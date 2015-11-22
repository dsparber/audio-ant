import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import csv.CsvReader;
import microphone.AudioReader;
import parameters.AudioAnalysisParameter;
import parameters.AudioParamters;
import parameters.WorkingDirectory;
import tools.MaxSizeArrayList;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class AnalyseStream extends Observable implements Observer {

	private AudioReader reader;

	private double[] savedFreq;
	private MaxSizeArrayList<Double> recentFreq;

	private RConnection rConnection;

	public AnalyseStream() throws RserveException, IOException {

		recentFreq = new MaxSizeArrayList<Double>(AudioAnalysisParameter.NUMBER_COMPARISON_WINDOWS);

		rConnection = new RConnection();
		rConnection.eval("library(tuneR)");
		rConnection.eval("library(seewave)");

		CsvReader reader = new CsvReader(WorkingDirectory.FOLDER + WorkingDirectory.FEATURES_CSV);

		String[] csvValues = reader.readSingleCol(0);

		savedFreq = new double[csvValues.length];

		for (int i = 0; i < csvValues.length; i++) {
			savedFreq[i] = Double.parseDouble(csvValues[i]);
		}
	}

	public void start() {
		reader = new AudioReader();
		reader.addObserver(this);
		reader.streamWindows();
	}

	public void stop() {
		reader.stop();
	}

	@Override
	public void update(Observable o, Object arg) {

		int[] samples = (int[]) arg;

		try {

			rConnection.assign("window", samples);
			rConnection.eval("wave <- Wave(window, samp.rate = " + AudioParamters.SAMPLE_RATE + ")");
			rConnection.eval("spec <- meanspec(wave, plot = FALSE)");
			double strongestFreq = rConnection.eval("specprop(spec)$mode").asDouble();

			recentFreq.add(strongestFreq);

			setChanged();
			if (recentFreqMatch()) {
				notifyObservers(true);
			} else {
				notifyObservers(false);
			}

		} catch (REngineException | REXPMismatchException e) {
			e.printStackTrace();
		}

	}

	private boolean recentFreqMatch() {

		double strongestFreq = recentFreq.get(recentFreq.size() - 1);

		for (int i = 0; i < savedFreq.length; i++) {
			double min = savedFreq[i] - (AudioAnalysisParameter.DETECTION_THRESHOLD);
			double max = savedFreq[i] + (AudioAnalysisParameter.DETECTION_THRESHOLD);

			if (strongestFreq < max && strongestFreq > min) {
				return true;
			}
		}
		return false;
	}

}
