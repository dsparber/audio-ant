package audio.analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RserveException;

import audio.parameters.AudioAnalysisParameter;
import io.csv.CsvReader;
import io.microphone.AudioStreamReader;
import io.parameters.WorkingDirectory;
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

	protected double[] savedFreqs;
	protected ArrayList<Double> recentFreqs;

	public AudioAnalyser() throws RserveException, IOException {

		CsvReader reader = new CsvReader(WorkingDirectory.FOLDER + WorkingDirectory.FEATURES_CSV);
		String[] csvValues = reader.readSingleCol(0);

		savedFreqs = new double[csvValues.length];

		for (int i = 0; i < csvValues.length; i++) {
			savedFreqs[i] = Double.parseDouble(csvValues[i]);
		}

		recentFreqs = new MaxSizeArrayList<Double>(savedFreqs.length);
	}

	protected void addRecentFreq(int[] samples, float sampleRate) throws REngineException, REXPMismatchException {
		WindowAnalyser analyser = new WindowAnalyser();
		analyser.assignSamples(samples, sampleRate);
		double strongestFreq = analyser.getStrongestFrequency();

		recentFreqs.add(strongestFreq);
	}

	protected double getFreqMatch() {

		int matches1 = 0;

		for (double savedFreq : savedFreqs) {

			for (double recentFreq : recentFreqs) {

				double min = savedFreq - AudioAnalysisParameter.DETECTION_THRESHOLD;
				double max = savedFreq + AudioAnalysisParameter.DETECTION_THRESHOLD;

				if (recentFreq < max && recentFreq > min) {
					matches1++;
					break;
				}
			}
		}

		int matches2 = 0;

		for (double recentFreq : recentFreqs) {

			for (double savedFreq : savedFreqs) {

				double min = savedFreq - AudioAnalysisParameter.DETECTION_THRESHOLD;
				double max = savedFreq + AudioAnalysisParameter.DETECTION_THRESHOLD;

				if (recentFreq < max && recentFreq > min) {
					matches2++;
					break;
				}
			}
		}

		return matches1 * matches2 / (double) (savedFreqs.length * recentFreqs.size());
	}
}