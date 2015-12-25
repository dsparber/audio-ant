package audio.analysis.features.strongestFrequency;

import java.io.IOException;
import java.util.ArrayList;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RserveException;

import audio.analysis.WindowAnalyser;
import config.Parameters.WorkingDir;
import io.csv.CsvReader;
import tools.MaxSizeArrayList;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class FrequnecyAnalyser {

	protected StrongestFrequenciesModel[] savedFreqs;
	protected ArrayList<StrongestFrequenciesModel> recentFreqs;

	private WindowAnalyser analyser;

	public FrequnecyAnalyser(WindowAnalyser analyser) throws RserveException, IOException {

		this.analyser = analyser;

		savedFreqs = loadCsvValues();

		recentFreqs = new MaxSizeArrayList<StrongestFrequenciesModel>(savedFreqs.length);
	}

	public void analyseSamples() throws REngineException, REXPMismatchException {

		StrongestFrequenciesModel strongestFreq = analyser.getStrongestFrequencies();
		recentFreqs.add(strongestFreq);
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

		CsvReader reader = new CsvReader(WorkingDir.FREQUENCIES_CSV);
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
}