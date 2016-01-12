package com.audioant.audio.analysis.sound.strongestFrequency;

import java.util.ArrayList;
import java.util.List;

import com.audioant.config.Parameters.Audio.Analysis;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class StrongestFrequenciesModel {

	private List<FrequencyModel> strongestFrequencies;

	public StrongestFrequenciesModel() {
		strongestFrequencies = new ArrayList<FrequencyModel>();
	}

	public void addFrequency(FrequencyModel model) {

		if (model.getFrequency() >= Analysis.MIN_FREQ && model.getFrequency() <= Analysis.MAX_FREQ) {
			strongestFrequencies.add(model);
		}
	}

	public double overlap(StrongestFrequenciesModel model) {

		double c1 = 0, s = 0;
		for (FrequencyModel m : strongestFrequencies) {
			if (model.strongestFrequencies.contains(m)) {
				c1 += m.getAmplitude();
			}
			s += m.getAmplitude();
		}
		c1 /= s;
		s = 0;

		double c2 = 0;
		for (FrequencyModel m : model.strongestFrequencies) {
			if (strongestFrequencies.contains(m)) {
				c2 += m.getAmplitude();
			}
			s += m.getAmplitude();
		}
		c2 /= s;

		double result = c1 * c2;

		return result;
	}

	public int size() {
		return strongestFrequencies.size();
	}

	public FrequencyModel get(int index) {
		return strongestFrequencies.get(index);
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();

		for (FrequencyModel frequencyModel : strongestFrequencies) {
			sb.append(frequencyModel.toString());
			sb.append(",");
		}
		return sb.toString();
	}
}
