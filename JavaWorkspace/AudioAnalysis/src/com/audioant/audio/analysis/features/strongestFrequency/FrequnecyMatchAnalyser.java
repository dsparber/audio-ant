package com.audioant.audio.analysis.features.strongestFrequency;

import java.io.IOException;
import java.util.ArrayList;

import org.rosuda.REngine.Rserve.RserveException;

import com.audioant.audio.model.SoundModel;
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

	private SoundModel soundModel;

	public FrequnecyMatchAnalyser(SoundModel soundModel) throws RserveException, IOException {

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

		CsvReader reader = new CsvReader(soundModel.getFolder() + WorkingDir.FREQUENCIES_CSV);
		String[][] csvValues = reader.readMatrix();

		savedFreqs = new StrongestFrequenciesModel[csvValues.length];

		for (int i = 0; i < csvValues.length; i++) {

			StrongestFrequenciesModel model = new StrongestFrequenciesModel();

			for (int j = 0; j < csvValues[i].length; j++) {

				if (!csvValues[i][j].isEmpty()) {
					model.addFrequency(new FrequencyModel(csvValues[i][j]));
				}
			}

			savedFreqs[i] = model;
		}
		return savedFreqs;
	}

	public void addValue(StrongestFrequenciesModel strongestFrequencies) {
		recentFreqs.add(strongestFrequencies);
	}
}