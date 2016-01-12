package com.audioant.audio.analysis.sound.srp;

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
public class SrpAnalyser {

	private WindowAnalyser analyser;

	public SrpAnalyser(WindowAnalyser analyser) throws RserveException, IOException {

		this.analyser = analyser;
	}

	public double analyseSamples() throws REngineException, REXPMismatchException {

		double value = analyser.getSpectralRolloffPoint();
		return value;
	}
}