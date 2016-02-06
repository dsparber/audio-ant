package com.audioant.audio.analysis.match.energy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.rosuda.REngine.Rserve.RserveException;

import com.audioant.audio.model.Sound;
import com.audioant.config.Config;
import com.audioant.io.csv.CsvReader;
import com.audioant.tools.MaxSizeArrayList;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class EnergyMatchAnalyser {

	protected double[] savedValues;
	protected ArrayList<Double> recentValues;

	private Sound soundModel;

	public EnergyMatchAnalyser(Sound soundModel) throws RserveException, IOException {

		this.soundModel = soundModel;

		savedValues = loadCsvValues();

		recentValues = new MaxSizeArrayList<Double>(savedValues.length);
	}

	public double getMatch() {

		List<Double> recentValues = new ArrayList<Double>();

		double max = 0;
		for (Double d : this.recentValues) {
			if (d > max) {
				max = d;
			}
		}
		for (int i = 0; i < this.recentValues.size(); i++) {
			recentValues.add((this.recentValues.get(i) / max));
		}

		int startIndex = 0;
		for (Double d : recentValues) {

			if (Math.abs(savedValues[0] - d) <= Config.AUDIO_ANALYSIS_ENERGY_TOLERANCE) {
				break;
			}
			startIndex++;
		}

		List<Double> sublist = recentValues.subList(startIndex, recentValues.size());

		int count = 0;
		for (int i = 0; i < sublist.size(); i++) {

			if (Math.abs(savedValues[i] - sublist.get(i)) <= Config.AUDIO_ANALYSIS_ENERGY_TOLERANCE) {
				count++;
			}
		}

		return (double) count / savedValues.length;
	}

	private double[] loadCsvValues() throws IOException {

		CsvReader reader = new CsvReader(soundModel.getPath() + Config.LEARNED_SOUNDS_FILE_POWER);
		String[] csvValues = reader.readSingleCol(0);

		double[] values = new double[csvValues.length];

		for (int i = 0; i < csvValues.length; i++) {
			values[i] = Double.parseDouble(csvValues[i]);
		}

		return values;
	}

	public void addValue(double energy) {
		recentValues.add(energy);
	}
}