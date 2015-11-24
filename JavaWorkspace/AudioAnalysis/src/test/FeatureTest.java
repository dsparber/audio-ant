package test;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;

import audio.analysis.AudioFileAnalyser;
import audio.learning.SoundFileLearner;

public class FeatureTest {

	private String testingDir;

	public FeatureTest(String testingDir) {
		this.testingDir = testingDir;
	}

	public void analyseFiles() throws LineUnavailableException, IOException, REngineException, REXPMismatchException {

		SoundFileLearner learner = new SoundFileLearner(testingDir + "00.wav");
		learner.extractFeatures();

		File directory = new File(testingDir);

		for (File file : directory.listFiles()) {

			if (fileShouldBeAnalysed(file.getName())) {

				AudioFileAnalyser analyser = new AudioFileAnalyser(file.getAbsolutePath());
				double result = analyser.getMaxMatch();

				System.out.printf("%s:\t%.2f%%\n", file.getName(), result * 100);
			}
		}
	}

	private boolean fileShouldBeAnalysed(String fileName) {
		return !fileName.startsWith("00") && fileName.endsWith(".wav");
	}
}
