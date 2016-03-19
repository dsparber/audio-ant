package com.audioant.audio.analysis.sound;

import java.io.IOException;
import java.util.Observable;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RserveException;

import com.audioant.audio.analysis.WindowAnalyser;
import com.audioant.audio.analysis.sound.energy.EnergyAnalyser;
import com.audioant.audio.analysis.sound.mfcc.MfccAnalyser;
import com.audioant.audio.analysis.sound.srp.SrpAnalyser;
import com.audioant.audio.analysis.sound.strongestFrequency.FrequnecyAnalyser;
import com.audioant.audio.analysis.sound.strongestFrequency.StrongestFrequenciesModel;
import com.audioant.audio.model.Result;
import com.audioant.config.Config;
import com.audioant.io.microphone.AudioStreamReader;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class SoundAnalyser extends Observable {

	protected AudioStreamReader reader;

	private WindowAnalyser analyser;

	private FrequnecyAnalyser frequnecyAnalyser;
	private EnergyAnalyser energyAnalyser;
	private MfccAnalyser mfccAnalyser;
	private SrpAnalyser srpAnalyser;

	public SoundAnalyser() throws RserveException, IOException {

		analyser = new WindowAnalyser();

		frequnecyAnalyser = new FrequnecyAnalyser(analyser);
		energyAnalyser = new EnergyAnalyser(analyser);
		mfccAnalyser = new MfccAnalyser(analyser);
		srpAnalyser = new SrpAnalyser(analyser);
	}

	public Result analyseSamples(int[] samples, float sampleRate) throws REngineException, REXPMismatchException {

		analyser.assignSamples(samples, sampleRate);
		analyser.generateSpectrum();

		double energy = energyAnalyser.analyseSamples();
		if (energy >= Config.AUDIO_ANALYSIS_POWER_MIN_RMS) {

			StrongestFrequenciesModel strongestFreq = frequnecyAnalyser.analyseSamples();
			if (strongestFreq.size() > 0 && strongestFreq.size() <= Config.AUDIO_ANALYSIS_FREQUENCY_MAX_PEAK_COUNT) {

				double srp = srpAnalyser.analyseSamples();
				double[] mfcc = mfccAnalyser.analyseSamples();

				return new Result(strongestFreq, srp, mfcc, energy);
			}
		}

		return null;
	}
}