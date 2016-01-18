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
import com.audioant.audio.model.Result;
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

		return new Result(frequnecyAnalyser.analyseSamples(), srpAnalyser.analyseSamples(),
				mfccAnalyser.analyseSamples(), energyAnalyser.analyseSamples());
	}
}