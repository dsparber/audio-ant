package com.audioant.audio.model;

import com.audioant.audio.analysis.features.strongestFrequency.StrongestFrequenciesModel;

public class ResultModel {

	private StrongestFrequenciesModel strongestFrequencies;
	private double spectralRolloffPoint;

	public ResultModel(StrongestFrequenciesModel strongestFrequencies, double spectralRolloffPoint) {
		super();
		this.strongestFrequencies = strongestFrequencies;
		this.spectralRolloffPoint = spectralRolloffPoint;
	}

	public StrongestFrequenciesModel getStrongestFrequencies() {
		return strongestFrequencies;
	}

	public double getSpectralRolloffPoint() {
		return spectralRolloffPoint;
	}

}
