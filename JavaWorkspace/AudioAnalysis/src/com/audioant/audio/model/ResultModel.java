package com.audioant.audio.model;

import com.audioant.audio.analysis.features.strongestFrequency.StrongestFrequenciesModel;

public class ResultModel {

	private StrongestFrequenciesModel strongestFrequencies;
	private double spectralRolloffPoint;
	private double[] mfcc;

	public ResultModel(StrongestFrequenciesModel strongestFrequencies, double spectralRolloffPoint, double[] mfcc) {
		super();
		this.strongestFrequencies = strongestFrequencies;
		this.spectralRolloffPoint = spectralRolloffPoint;
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
}
