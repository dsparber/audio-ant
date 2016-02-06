package com.audioant.audio.analysis.match.mfcc;

import java.io.IOException;
import java.util.ArrayList;

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
public class MfccMatchAnalyser {

	protected double[][] savedValues;
	protected ArrayList<double[]> recentValues;

	private double[] savedMeans;

	private Sound soundModel;

	public MfccMatchAnalyser(Sound soundModel) throws RserveException, IOException {

		this.soundModel = soundModel;

		savedValues = loadCsvValues();

		savedMeans = new double[savedValues[0].length];

		for (int j = 0; j < savedValues[0].length; j++) {
			double tmp = 0;
			for (int i = 0; i < savedValues.length; i++) {
				if (!Double.isNaN(savedValues[i][j])) {
					tmp += savedValues[i][j];
				}
			}
			savedMeans[j] = tmp / savedValues.length;
		}

		recentValues = new MaxSizeArrayList<double[]>(savedValues.length);
	}

	public double getMatch() {

		double[] recentMeans = new double[recentValues.get(0).length];

		for (int j = 0; j < recentValues.get(0).length; j++) {
			double tmp = 0;
			for (int i = 0; i < recentValues.size(); i++) {
				if (!Double.isNaN(savedValues[i][j])) {
					tmp += recentValues.get(i)[j];
				}
			}
			recentMeans[j] = tmp / recentValues.size();
		}

		return getMatch(savedMeans, recentMeans);
	}

	private double getMatch(double[] d1, double[] d2) {

		int count = 0, countTrue = 0;

		for (int i = 0; i < d1.length; i++) {

			if (Config.AUDIO_ANALYSIS_MFCC_COEFFICIENT_USED[i]) {
				if (Math.abs(d1[i] - d2[i]) <= Config.AUDIO_ANALYSIS_MFCC_TOLERANCE[i]) {
					countTrue++;
				}
				count++;
			}
		}

		return (double) countTrue / count;
	}

	private double[][] loadCsvValues() throws IOException {

		CsvReader reader = new CsvReader(soundModel.getPath() + Config.LEARNED_SOUNDS_FILE_MFCC);
		String[][] csvValues = reader.readMatrix();

		double[][] values = new double[csvValues.length][csvValues[0].length];

		for (int i = 0; i < csvValues.length; i++) {
			for (int k = 0; k < values[i].length; k++) {
				values[i][k] = Double.parseDouble(csvValues[i][k].replace(',', '.'));
			}
		}

		return values;
	}

	public void addValue(double[] mfcc) {
		recentValues.add(mfcc);
	}
}