package com.audioant.audio.learning;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;

import com.audioant.audio.analysis.WindowAnalyser;
import com.audioant.audio.learning.features.spectralRolloffPoint.SrpLearner;
import com.audioant.audio.learning.features.strongestFrequency.StrongestFrequencyLearner;
import com.audioant.audio.windowing.Windowing;
import com.audioant.config.Parameters.Audio;
import com.audioant.config.Parameters.Audio.Analysis;
import com.audioant.io.audio.AudioFileReader;
import com.audioant.io.audio.AudioFileReaderFactory;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */

public abstract class SoundLearner {

	protected String soundfile;

	private WindowAnalyser windowAnalyser = new WindowAnalyser();

	private SrpLearner srpLearner = new SrpLearner(windowAnalyser);
	private StrongestFrequencyLearner frequencyLearner = new StrongestFrequencyLearner(windowAnalyser);

	public void extractFeatures() throws LineUnavailableException, IOException, REngineException, REXPMismatchException,
			UnsupportedAudioFileException {

		AudioFileReader reader = AudioFileReaderFactory.getFileReader(soundfile);

		int[][] windows = Windowing.createWindows(reader.readData(), Audio.WINDOW_SIZE, 0f);
		float sampleRate = reader.getSampleRate();

		for (int samples[] : windows) {

			windowAnalyser.assignSamples(samples, sampleRate);

			if (windowAnalyser.getEnergy() > Analysis.MIN_ENERGY) {

				windowAnalyser.generateSpectrum();

				if (frequencyLearner.analyseWindow()) {
					srpLearner.analyseWindow();
				}
			}
		}

		frequencyLearner.saveFeatures();
		srpLearner.saveFeatures();
	}
}
