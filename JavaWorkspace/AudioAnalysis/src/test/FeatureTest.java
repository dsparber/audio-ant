package test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.sound.sampled.LineUnavailableException;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;

import audio.analysis.AudioFileAnalyser;
import audio.learning.SoundFileLearner;
import config.Parameters.Audio.Analysis;
import config.Parameters.AutomatedTest;
import io.sql.AutomatedTestDatabaseManager;
import test.model.FeatureMatchModel;
import test.model.ResultModel;
import test.model.SoundModel;
import test.model.TestModel;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class FeatureTest {

	private String testingDir;

	private AutomatedTestDatabaseManager manager;
	private TestModel test;

	public FeatureTest(String testingDir) {
		this.testingDir = testingDir;
	}

	public void analyseFiles()
			throws LineUnavailableException, IOException, REngineException, REXPMismatchException, SQLException {

		manager = new AutomatedTestDatabaseManager();

		test = new TestModel();
		manager.insert(test);

		File[] sounds = new File(testingDir).listFiles();

		for (File sound : sounds) {
			if (sound.isDirectory()) {
				analyseSound(sound);
			}
		}
	}

	private boolean fileShouldBeAnalysed(String fileName) {
		return !fileName.matches(AutomatedTest.NAME_PATTERN_REFERENCE_FILE);
	}

	private void analyseSound(File soundDir)
			throws LineUnavailableException, IOException, REngineException, REXPMismatchException, SQLException {

		String soundName = soundDir.getName();

		SoundModel sound = new SoundModel(soundName);
		manager.insert(sound);

		// Learn sound
		for (File file : soundDir.listFiles()) {

			if (file.getName().matches(AutomatedTest.NAME_PATTERN_REFERENCE_FILE)) {

				SoundFileLearner learner = new SoundFileLearner(file.getAbsolutePath());
				learner.extractFeatures();

				break;
			}
		}

		// Analyse files
		for (File file : soundDir.listFiles()) {

			String fileName = file.getName();

			if (fileShouldBeAnalysed(fileName)) {
				if (file.getName().matches(AutomatedTest.NAME_PATTERN_SOUND_FILE)) {
					analyseFile(file, sound);
				}
			}
		}
	}

	private void analyseFile(File file, SoundModel sound)
			throws IOException, REngineException, REXPMismatchException, SQLException {

		AudioFileAnalyser analyser = new AudioFileAnalyser(file.getAbsolutePath());
		System.out.println(file.getAbsolutePath());
		double matchResult = analyser.getMaxMatch();

		boolean wasRecognised = matchResult >= Analysis.MATCH_THRESHOLD;

		ResultModel result = new ResultModel(test.getId(), sound.getId(), file.getName(), wasRecognised);
		manager.insert(result);

		FeatureMatchModel match = new FeatureMatchModel(result.getId(), matchResult);
		manager.insert(match);
	}
}