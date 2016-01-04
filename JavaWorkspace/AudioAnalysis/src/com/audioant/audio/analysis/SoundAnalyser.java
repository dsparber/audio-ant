package com.audioant.audio.analysis;

import java.io.IOException;
import java.util.Observable;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RserveException;

import com.audioant.audio.analysis.features.mfcc.MfccAnalyser;
import com.audioant.audio.analysis.features.spectralRolloffPoint.SrpAnalyser;
import com.audioant.audio.analysis.features.strongestFrequency.FrequnecyAnalyser;
import com.audioant.audio.model.ResultModel;
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
	private SrpAnalyser srpAnalyser;
	private MfccAnalyser mfccAnalyser;

	public SoundAnalyser() throws RserveException, IOException {

		analyser = new WindowAnalyser();

		frequnecyAnalyser = new FrequnecyAnalyser(analyser);
		mfccAnalyser = new MfccAnalyser(analyser);
		srpAnalyser = new SrpAnalyser(analyser);
	}

	protected ResultModel analyseSamples(int[] samples, float sampleRate)
			throws REngineException, REXPMismatchException {

		analyser.assignSamples(samples, sampleRate);
		analyser.generateSpectrum();

		return new ResultModel(frequnecyAnalyser.analyseSamples(), srpAnalyser.analyseSamples(),
				mfccAnalyser.analyseSamples());
	}
}