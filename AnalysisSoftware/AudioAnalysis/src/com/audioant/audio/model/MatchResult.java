package com.audioant.audio.model;

/**
 *
 * @author Daniel Sparber
 * @year 2016
 *
 * @version 1.0
 */
public class MatchResult {

	private boolean match;
	private boolean sureMatch;

	private boolean matchByWeightedSum;
	private boolean matchByThresholds;
	private boolean minThresholdReached;

	private double weigthedSum;

	private double frequencyMatch;
	private double energyMatch;
	private double mfccMatch;
	private double srpMatch;

	private Sound sound;

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();

		builder.append("Sound: ");
		builder.append(sound.getNumber());
		builder.append("\t");

		if (sureMatch) {
			builder.append("sure\t");
		} else if (match) {
			builder.append("match\t");
		} else if (minThresholdReached) {
			builder.append("min \t");
		}

		builder.append("by Sum: ");
		builder.append(matchByWeightedSum);
		builder.append("\tby Threshold: ");
		builder.append(matchByThresholds);

		builder.append(String.format("\t\tsum: %.2f\t\t", weigthedSum));

		builder.append(String.format("f: %.2f \t", frequencyMatch));
		builder.append(String.format("e: %.2f \t", energyMatch));
		builder.append(String.format("m: %.2f \t", mfccMatch));
		builder.append(String.format("s: %.2f", srpMatch));

		return builder.toString();
	}

	public MatchResult(boolean match, boolean sureMatch, boolean matchByWeightedSum, boolean matchByThresholds,
			boolean minThresholdReached, double weigthedSum, double frequencyMatch, double energyMatch,
			double mfccMatch, double srpMatch, Sound sound) {
		super();
		this.match = match;
		this.sureMatch = sureMatch;
		this.matchByWeightedSum = matchByWeightedSum;
		this.matchByThresholds = matchByThresholds;
		this.minThresholdReached = minThresholdReached;
		this.weigthedSum = weigthedSum;
		this.frequencyMatch = frequencyMatch;
		this.energyMatch = energyMatch;
		this.mfccMatch = mfccMatch;
		this.srpMatch = srpMatch;
		this.sound = sound;
	}

	public boolean isMatch() {
		return match;
	}

	public boolean isSureMatch() {
		return sureMatch;
	}

	public boolean isMatchByWeightedSum() {
		return matchByWeightedSum;
	}

	public boolean isMatchByThresholds() {
		return matchByThresholds;
	}

	public boolean isMinThresholdReached() {
		return minThresholdReached;
	}

	public double getWeigthedSum() {
		return weigthedSum;
	}

	public double getFrequencyMatch() {
		return frequencyMatch;
	}

	public double getEnergyMatch() {
		return energyMatch;
	}

	public double getMfccMatch() {
		return mfccMatch;
	}

	public double getSrpMatch() {
		return srpMatch;
	}

	public Sound getSound() {
		return sound;
	}
}
