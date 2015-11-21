import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import csv.CsvReader;
import microphone.AudioReader;
import parameters.AudioAnalysisParameter;
import parameters.WorkingDirectory;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class AnalyseStream implements Observer {

	private AudioReader reader;

	private double[] savedFreq;

	private RConnection rConnection;

	public AnalyseStream() throws RserveException, IOException {
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
		AudioReader reader = new AudioReader();
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
			rConnection.eval("wave <- Wave(window)");
			rConnection.eval("spec <- meanspec(wave, plot = FALSE)");
			REXP strongestFreq = rConnection.eval("specprop(spec)$mode");

			double min = savedFreq[0] - (AudioAnalysisParameter.DETECTION_THRESHOLD);
			double max = savedFreq[0] + (AudioAnalysisParameter.DETECTION_THRESHOLD);

			System.out.print(min);
			System.out.print("\t");
			System.out.print(max);
			System.out.print("\t");
			System.out.println(strongestFreq.asDouble());

			if (strongestFreq.asDouble() > max && strongestFreq.asDouble() < min) {
				System.out.println("Sound occured");
			}

		} catch (REngineException | REXPMismatchException e) {
			e.printStackTrace();
		}

	}

}
