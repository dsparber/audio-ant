package audio.analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RserveException;

import audio.analysis.model.FrequencyModel;
import audio.analysis.model.StrongestFrequenciesModel;
import config.Parameters.WorkingDir;
import io.csv.CsvReader;
import io.microphone.AudioStreamReader;
import tools.MaxSizeArrayList;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class AudioAnalyser extends Observable {

	protected AudioStreamReader reader;

	protected StrongestFrequenciesModel[] savedFreqs;
	protected ArrayList<StrongestFrequenciesModel> recentFreqs;

	public AudioAnalyser() throws RserveException, IOException {

		CsvReader reader = new CsvReader(WorkingDir.FOLDER_LEARNED_SOUNDS + WorkingDir.FEATURES_CSV);
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

		recentFreqs = new MaxSizeArrayList<StrongestFrequenciesModel>(savedFreqs.length);
	}

	protected void addRecentFreq(int[] samples, float sampleRate) throws REngineException, REXPMismatchException {

		WindowAnalyser analyser = new WindowAnalyser();
		analyser.assignSamples(samples, sampleRate);
		StrongestFrequenciesModel strongestFreq = analyser.getStrongestFrequencies();

		recentFreqs.add(strongestFreq);
	}

	protected double getFreqMatch() {

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
}