package com.audioant.test.model;

/**
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class FeatureMatchModel {

	private int id;
	private int resultId;

	private double strongestFrequencyMatch;
	private double spectralRolloffPointMatch;
	private double mfccMatch;
	private double energyMatch;

	public FeatureMatchModel(int resultId, double strongestFrequencyMatch, double spectralRolloffPointMatch,
			double mfccMatch, double energyMatch) {
		this.resultId = resultId;
		this.mfccMatch = mfccMatch;
		this.strongestFrequencyMatch = strongestFrequencyMatch;
		this.spectralRolloffPointMatch = spectralRolloffPointMatch;
		this.energyMatch = energyMatch;
	}

	public int getResultId() {
		return resultId;
	}

	public double getStrongestFrequencyMatch() {
		return strongestFrequencyMatch;
	}

	public int getId() {
		return id;
	}

	public double getSpectralRolloffPointMatch() {
		return spectralRolloffPointMatch;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getMfccMatch() {
		return mfccMatch;
	}

	public double getEnergyMatch() {
		return energyMatch;
	}
}