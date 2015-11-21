import java.io.IOException;
import java.util.Scanner;

import javax.sound.sampled.LineUnavailableException;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;

import csv.CsvWriter;
import microphone.RecordAudio;
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

	public void learnSound() throws LineUnavailableException, IOException, REngineException, REXPMismatchException {

		recordAudio();

		System.out.println("Extracting features...");
		Double[] strongestFreq = extractStrongestFrequency();

		CsvWriter writer = new CsvWriter(WorkingDirectory.FOLDER + WorkingDirectory.FEATURES_CSV);
		writer.writeSingleColmn(strongestFreq);
		System.out.println("Features extracted");
	}

	private void recordAudio() throws LineUnavailableException {
		RecordAudio recordAudio = new RecordAudio(WorkingDirectory.FOLDER + WorkingDirectory.AUDIO_FILE);

		Scanner s = new Scanner(System.in);
		System.out.println("Hit return to start recording");
		s.nextLine();
		System.out.println("Start recording (Hit return to end)");
		recordAudio.startCapturing();
		s.nextLine();
		recordAudio.stopCapture();

		System.out.println("Recording ended");
		s.close();
	}

	private Double[] extractStrongestFrequency() throws IOException, REngineException, REXPMismatchException {

		WavAudioFileReader reader = new WavAudioFileReader(WorkingDirectory.FOLDER + WorkingDirectory.AUDIO_FILE);

		int[][] windows = Windowing.createWindows(reader.readData(), AudioParamters.WINDOW_SIZE, 0f);

		RConnection rConnection = new RConnection();

		rConnection.eval("library(tuneR)");
		rConnection.eval("library(seewave)");

		Double[] result = new Double[windows.length];

		for (int i = 0; i < windows.length; i++) {

			rConnection.assign("window", windows[i]);
			rConnection.eval("wave <- Wave(window, samp.rate = " + AudioParamters.SAMPLE_RATE + ")");
			rConnection.eval("spec <- meanspec(wave, plot = FALSE)");
			REXP strongestFreq = rConnection.eval("specprop(spec)$mode");
			result[i] = strongestFreq.asDouble();
		}

		return result;
	}
}
