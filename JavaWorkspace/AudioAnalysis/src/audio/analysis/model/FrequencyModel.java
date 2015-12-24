package audio.analysis.model;

import config.Parameters.Audio.Analysis;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class FrequencyModel {

	private double frequency;
	private double amplitude;

	private final static String SEPERATOR = "/";

	public FrequencyModel(String values) {
		frequency = Double.parseDouble(values.split(SEPERATOR)[0]);
		amplitude = Double.parseDouble(values.split(SEPERATOR)[1]);
	}

	public FrequencyModel(double frequency, double amplitude) {
		this.frequency = frequency;
		this.amplitude = amplitude;
	}

	public double getFrequency() {
		return frequency;
	}

	public double getAmplitude() {
		return amplitude;
	}

	@Override
	public String toString() {
		return frequency + SEPERATOR + amplitude;
	}

	@Override
	public boolean equals(Object obj) {
		FrequencyModel model = (FrequencyModel) obj;

		double min = frequency - Analysis.DETECTION_THRESHOLD;
		double max = frequency + Analysis.DETECTION_THRESHOLD;

		return model.frequency >= min && model.frequency <= max;
	}
}
