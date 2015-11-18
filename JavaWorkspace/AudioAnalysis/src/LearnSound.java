import java.io.IOException;
import java.util.Scanner;

import javax.sound.sampled.LineUnavailableException;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;

import csv.CsvWriter;
import recording.RecordAudio;
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

	private static final String FOLDER = "/Users/daniel/Desktop/";
	private static final String AUDIO_FILE = "out.wav";
	private static final String CSV_FILE = "features.csv";

	public static void main(String[] args)
			throws LineUnavailableException, IOException, REngineException, REXPMismatchException {

		recordAudio();

		Double[] strongestFreq = extractStrongestFrequency();

		CsvWriter writer = new CsvWriter(FOLDER + CSV_FILE);
		writer.writeSingleColmn(strongestFreq);

		System.out.println("Program terminated");
	}

	private static void recordAudio() throws LineUnavailableException {
		RecordAudio recordAudio = new RecordAudio(FOLDER + AUDIO_FILE);

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

	private static Double[] extractStrongestFrequency() throws IOException, REngineException, REXPMismatchException {

		WavAudioFileReader reader = new WavAudioFileReader(FOLDER + AUDIO_FILE);

		int[][] windows = Windowing.createWindows(reader.readData(), 2048, 0f);

		RConnection rConnection = new RConnection();

		rConnection.eval("library(tuneR)");
		rConnection.eval("library(seewave)");

		Double[] result = new Double[windows.length];

		for (int i = 0; i < windows.length; i++) {

			rConnection.assign("window", windows[i]);
			rConnection.eval("wave <- Wave(window)");
			rConnection.eval("spec <- meanspec(wave, plot = FALSE)");
			REXP strongestFreq = rConnection.eval("specprop(spec)$mode");
			result[i] = strongestFreq.asDouble();
		}

		return result;
	}
}
