package com.audioant.audio.analysis.sound.strongestFrequency;

import java.io.IOException;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RserveException;

import com.audioant.audio.analysis.WindowAnalyser;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class FrequnecyAnalyser {

	private WindowAnalyser analyser;

	public FrequnecyAnalyser(WindowAnalyser analyser) throws RserveException, IOException {

		this.analyser = analyser;
	}

	public StrongestFrequenciesModel analyseSamples() throws REngineException, REXPMismatchException {

		StrongestFrequenciesModel strongestFreq = analyser.getStrongestFrequencies();
		return strongestFreq;
	}

}