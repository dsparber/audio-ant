package com.audioant.test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;

import com.audioant.audio.analysis.SoundFileAnalyser;
import com.audioant.audio.learning.SoundFileLearner;
import com.audioant.config.Config;
import com.audioant.io.sql.AutomatedTestDatabaseManager;
import com.audioant.test.model.FeatureMatchModel;
import com.audioant.test.model.ResultModel;
import com.audioant.test.model.SoundModel;
import com.audioant.test.model.TestModel;

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
	private String fileType;

	public FeatureTest(String testingDir, String fileType) {
		this.testingDir = testingDir;
		this.fileType = fileType;
	}

	public void analyseFiles() throws LineUnavailableException, IOException, REngineException, REXPMismatchException,
			SQLException, UnsupportedAudioFileException {

		long start = System.currentTimeMillis();

		manager = new AutomatedTestDatabaseManager();

		test = new TestModel(fileType);
		manager.insert(test);

		File[] sounds = new File(testingDir).listFiles();

		for (File sound : sounds) {
			if (sound.isDirectory()) {
				analyseSound(sound);
			}
		}

		System.out.printf("Duration: %ss\n", (System.currentTimeMillis() - start) / 1000.);
	}

	private boolean fileShouldBeAnalysed(String fileName) {
		return !fileName.matches(Config.AUTOTEST_PATTERN_REF);
	}

	private void analyseSound(File soundDir) throws LineUnavailableException, IOException, REngineException,
			REXPMismatchException, SQLException, UnsupportedAudioFileException {

		String soundName = soundDir.getName();

		SoundModel sound = new SoundModel(soundName);
		manager.insert(sound);

		// Learn sound
		for (File file : soundDir.listFiles()) {

			if (file.getName().matches(Config.AUTOTEST_PATTERN_REF)) {

				SoundFileLearner learner = new SoundFileLearner(file.getName(), file.getAbsolutePath());
				learner.extractFeatures();

				break;
			}
		}

		// Analyse files
		for (File file : soundDir.listFiles()) {

			String fileName = file.getName();

			if (fileShouldBeAnalysed(fileName)) {
				if (file.getName().matches(Config.AUTOTEST_PATTERN_TRUE)) {
					analyseFile(file, sound);
				}
			}
		}
	}

	private void analyseFile(File file, SoundModel sound)
			throws IOException, REngineException, REXPMismatchException, SQLException, UnsupportedAudioFileException {

		SoundFileAnalyser analyser = new SoundFileAnalyser(file.getName(), file.getAbsolutePath());
		System.out.println(file.getAbsolutePath());
		analyser.analyse();

		boolean wasRecognised = analyser.getMatch();

		ResultModel result = new ResultModel(test.getId(), sound.getId(), file.getName(), wasRecognised);
		manager.insert(result);

		FeatureMatchModel match = new FeatureMatchModel(result.getId(), analyser.getMaxFrequnecyMatch(),
				analyser.getMaxSrpMatch(), analyser.getMaxMfccMatch(), analyser.getMaxEnergyMatch());
		manager.insert(match);
	}
}
