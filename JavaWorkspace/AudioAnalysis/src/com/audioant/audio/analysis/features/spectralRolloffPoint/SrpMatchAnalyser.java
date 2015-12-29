package com.audioant.audio.analysis.features.spectralRolloffPoint;

import java.io.IOException;
import java.util.ArrayList;

import org.rosuda.REngine.Rserve.RserveException;

import com.audioant.audio.model.SoundModel;
import com.audioant.config.Parameters.Audio.Analysis;
import com.audioant.config.Parameters.WorkingDir;
import com.audioant.io.csv.CsvReader;
import com.audioant.tools.MaxSizeArrayList;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class SrpMatchAnalyser {

	protected double[] savedValues;
	protected ArrayList<Double> recentValues;

	private SoundModel soundModel;

	public SrpMatchAnalyser(SoundModel soundModel) throws RserveException, IOException {

		this.soundModel = soundModel;

		savedValues = loadCsvValues();

		recentValues = new MaxSizeArrayList<Double>(savedValues.length);
	}

	public double getMatch() {

		double c1 = 0;
		int count = 0;
		for (double savedValue : savedValues) {

			double max = 0;
			for (double recentValue : recentValues) {

				double tmp = getMatch(savedValue, recentValue);
				if (tmp > max) {
					max = tmp;
				}
			}
			c1 += max;
			count++;
		}
		c1 /= count;

		double c2 = 0;
		count = 0;
		for (double recentValue : recentValues) {

			double max = 0;
			for (double savedValue : savedValues) {

				double tmp = getMatch(recentValue, savedValue);
				if (tmp > max) {
					max = tmp;
				}
			}
			c2 += max;
			count++;
		}

		c2 /= count;

		double result = c1 * c2;

		return result;
	}

	private double getMatch(double d1, double d2) {

		double min = d1 * (1 - Analysis.SRP_TOLERANCE);
		double max = d1 * (1 + Analysis.SRP_TOLERANCE);

		return (d2 > min && d2 < max) ? 1 : 0;
	}

	private double[] loadCsvValues() throws IOException {

		CsvReader reader = new CsvReader(soundModel.getFolder() + WorkingDir.SRP_CSV);
		String[] csvValues = reader.readSingleCol(0);

		double[] values = new double[csvValues.length];

		for (int i = 0; i < csvValues.length; i++) {

			values[i] = Double.parseDouble(csvValues[i]);
		}

		return values;
	}

	public void addValue(double spectralRolloffPoint) {
		recentValues.add(spectralRolloffPoint);
	}
}