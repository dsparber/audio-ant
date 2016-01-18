package com.audioant.audio.analysis.match.strongestFrequency;

import java.io.IOException;
import java.util.ArrayList;

import org.rosuda.REngine.Rserve.RserveException;

import com.audioant.audio.analysis.sound.strongestFrequency.FrequencyModel;
import com.audioant.audio.analysis.sound.strongestFrequency.StrongestFrequenciesModel;
import com.audioant.audio.model.Sound;
import com.audioant.config.Parameters.Audio;
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
public class FrequnecyMatchAnalyser {

	protected StrongestFrequenciesModel[] savedFreqs;
	protected ArrayList<StrongestFrequenciesModel> recentFreqs;

	private Sound soundModel;

	private static double max = 0, min = Audio.SAMPLE_RATE;

	public FrequnecyMatchAnalyser(Sound soundModel) throws RserveException, IOException {

		this.soundModel = soundModel;

		savedFreqs = loadCsvValues();

		recentFreqs = new MaxSizeArrayList<StrongestFrequenciesModel>(savedFreqs.length);
	}

	public double getMatch() {

		double c1 = 0, count = 0;
		for (StrongestFrequenciesModel savedFreq : savedFreqs) {

			double max = 0;
			for (StrongestFrequenciesModel recentFreq : recentFreqs) {

				double tmp = savedFreq.overlap(recentFreq);
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
		for (StrongestFrequenciesModel recentFreq : recentFreqs) {

			double max = 0;
			for (StrongestFrequenciesModel savedFreq : savedFreqs) {

				double tmp = recentFreq.overlap(savedFreq);
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

	private StrongestFrequenciesModel[] loadCsvValues() throws IOException {

		StrongestFrequenciesModel[] savedFreqs;

		CsvReader reader = new CsvReader(soundModel.getPath() + WorkingDir.FREQUENCIES_CSV);
		String[][] csvValues = reader.readMatrix();

		savedFreqs = new StrongestFrequenciesModel[csvValues.length];

		for (int i = 0; i < csvValues.length; i++) {

			StrongestFrequenciesModel model = new StrongestFrequenciesModel();

			for (int j = 0; j < csvValues[i].length; j++) {

				if (!csvValues[i][j].isEmpty()) {
					model.addFrequency(new FrequencyModel(csvValues[i][j]));
					double freq = model.get(model.size() - 1).getFrequency();
					if (freq > max) {
						max = freq;
					}
					if (freq < min) {
						min = freq;
					}
				}
			}

			savedFreqs[i] = model;
		}
		return savedFreqs;
	}

	public void addValue(StrongestFrequenciesModel strongestFrequencies) {
		recentFreqs.add(strongestFrequencies);
	}

	public static double getMax() {
		return max + Analysis.BANDPASS_DELTA;
	}

	public static double getMin() {
		return min - Analysis.BANDPASS_DELTA;
	}
}