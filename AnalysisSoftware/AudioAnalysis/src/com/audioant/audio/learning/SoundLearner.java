package com.audioant.audio.learning;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;

import com.audioant.audio.analysis.WindowAnalyser;
import com.audioant.audio.learning.features.energy.EnergyLearner;
import com.audioant.audio.learning.features.mfcc.MfccLearner;
import com.audioant.audio.learning.features.spectralRolloffPoint.SrpLearner;
import com.audioant.audio.learning.features.strongestFrequency.StrongestFrequencyLearner;
import com.audioant.audio.model.Sound;
import com.audioant.audio.windowing.Windowing;
import com.audioant.config.Config;
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
	protected Sound soundModel;

	private WindowAnalyser windowAnalyser;

	private SrpLearner srpLearner;
	private MfccLearner mfccLearner;
	private EnergyLearner energyLearner;
	private StrongestFrequencyLearner frequencyLearner;

	public SoundLearner(Sound soundModel) {
		this.soundModel = soundModel;

		windowAnalyser = new WindowAnalyser();

		srpLearner = new SrpLearner(windowAnalyser, soundModel.getPath());
		mfccLearner = new MfccLearner(windowAnalyser, soundModel.getPath());
		energyLearner = new EnergyLearner(windowAnalyser, soundModel.getPath());
		frequencyLearner = new StrongestFrequencyLearner(windowAnalyser, soundModel.getPath());
	}

	public void extractFeatures() throws LineUnavailableException, IOException, REngineException, REXPMismatchException,
			UnsupportedAudioFileException {

		AudioFileReader reader = AudioFileReaderFactory.getFileReader(soundfile);

		int[][] windows = Windowing.createWindows(reader.readData(), Config.AUDIO_WINDOW_SIZE, 0f);
		float sampleRate = reader.getSampleRate();

		for (int samples[] : windows) {

			windowAnalyser.assignSamples(samples, sampleRate);
			windowAnalyser.generateSpectrum();

			if (energyLearner.shouldBeAnalysed() && frequencyLearner.shouldBeAnalysed()) {

				frequencyLearner.analyseWindow();
				energyLearner.analyseWindow();
				mfccLearner.analyseWindow();
				srpLearner.analyseWindow();
			}
		}

		frequencyLearner.saveFeatures();
		energyLearner.saveFeatures();
		mfccLearner.saveFeatures();
		srpLearner.saveFeatures();
	}
}
