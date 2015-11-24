package audio.learning;

import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.LineUnavailableException;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;

import audio.analysis.WindowAnalyser;
import audio.parameters.AudioAnalysisParameter;
import audio.parameters.AudioParamters;
import audio.windowing.Windowing;
import io.csv.CsvWriter;
import io.parameters.WorkingDirectory;
import io.wave.WavAudioFileReader;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */

public abstract class SoundLearner {

	protected String pathnameIn;
	protected String pathnameOut = WorkingDirectory.FOLDER + WorkingDirectory.FEATURES_CSV;

	public void extractFeatures()
			throws LineUnavailableException, IOException, REngineException, REXPMismatchException {

		Double[] strongestFreq = extractStrongestFrequency();

		CsvWriter writer = new CsvWriter(pathnameOut);
		writer.writeSingleColmn(strongestFreq);
	}

	protected Double[] extractStrongestFrequency() throws IOException, REngineException, REXPMismatchException {

		WavAudioFileReader reader = new WavAudioFileReader(pathnameIn);

		int[][] windows = Windowing.createWindows(reader.readData(), AudioParamters.WINDOW_SIZE, 0f);

		ArrayList<Double> results = new ArrayList<Double>();

		for (int samples[] : windows) {

			WindowAnalyser analyser = new WindowAnalyser();
			analyser.assignSamples(samples);
			double strongestFreq = analyser.getStrongestFrequency();

			if (strongestFreq >= AudioAnalysisParameter.MIN_FREQ && strongestFreq <= AudioAnalysisParameter.MAX_FREQ) {
				results.add(strongestFreq);
			}
		}

		Double[] result = new Double[results.size()];
		for (int j = 0; j < results.size(); j++) {
			result[j] = results.get(j);
		}

		return result;
	}
}
