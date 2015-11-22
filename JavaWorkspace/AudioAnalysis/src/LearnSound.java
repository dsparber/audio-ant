import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.LineUnavailableException;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;

import csv.CsvWriter;
import microphone.RecordAudio;
import parameters.AudioAnalysisParameter;
import parameters.AudioParamters;
import parameters.WorkingDirectory;
import wave.WavAudioFileReader;
import windowing.Windowing;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */

public class LearnSound {

	private RecordAudio recordAudio;

	public LearnSound() {
		recordAudio = new RecordAudio(WorkingDirectory.FOLDER + WorkingDirectory.AUDIO_FILE);
	}

	public void startCapturing() throws LineUnavailableException {
		recordAudio.startCapturing();
	}

	public void stopCapturing() {
		recordAudio.stopCapture();
	}

	public void extractFeatures()
			throws LineUnavailableException, IOException, REngineException, REXPMismatchException {

		Double[] strongestFreq = extractStrongestFrequency();

		CsvWriter writer = new CsvWriter(WorkingDirectory.FOLDER + WorkingDirectory.FEATURES_CSV);
		writer.writeSingleColmn(strongestFreq);
	}

	private Double[] extractStrongestFrequency() throws IOException, REngineException, REXPMismatchException {

		WavAudioFileReader reader = new WavAudioFileReader(WorkingDirectory.FOLDER + WorkingDirectory.AUDIO_FILE);

		int[][] windows = Windowing.createWindows(reader.readData(), AudioParamters.WINDOW_SIZE, 0f);

		RConnection rConnection = new RConnection();

		rConnection.eval("library(tuneR)");
		rConnection.eval("library(seewave)");

		ArrayList<Double> results = new ArrayList<Double>();

		for (int i = 0; i < windows.length; i++) {

			rConnection.assign("window", windows[i]);
			rConnection.eval("wave <- Wave(window, samp.rate = " + AudioParamters.SAMPLE_RATE + ")");
			rConnection.eval("spec <- meanspec(wave, plot = FALSE)");
			double strongestFreq = rConnection.eval("specprop(spec)$mode").asDouble();

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
