package com.audioant.audio.model;

import com.audioant.audio.analysis.sound.strongestFrequency.StrongestFrequenciesModel;

/**
 *
 * @author Daniel Sparber
 * @year 2016
 *
 * @version 1.0
 */
public class Result {

	private StrongestFrequenciesModel strongestFrequencies;
	private double spectralRolloffPoint;
	private double energy;
	private double[] mfcc;

	public Result(StrongestFrequenciesModel strongestFrequencies, double spectralRolloffPoint, double[] mfcc,
			double energy) {
		this.strongestFrequencies = strongestFrequencies;
		this.spectralRolloffPoint = spectralRolloffPoint;
		this.energy = energy;
		this.mfcc = mfcc;
	}

	public StrongestFrequenciesModel getStrongestFrequencies() {
		return strongestFrequencies;
	}

	public double getSpectralRolloffPoint() {
		return spectralRolloffPoint;
	}

	public double[] getMfcc() {
		return mfcc;
	}

	public double getEnergy() {
		return energy;
	}
}
