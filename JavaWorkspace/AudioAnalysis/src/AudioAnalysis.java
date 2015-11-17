import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;

import wave.WavAudioFileReader;
import windowing.Windowing;

public class AudioAnalysis {

	public static void main(String[] args)
			throws IOException, REXPMismatchException, REngineException, UnsupportedAudioFileException {

		int[] audioSignal = readAudioFileData("../../Audio Testfiles/Doorbell_d01.wav");

		int[][] windows = Windowing.generateHannWindows(audioSignal, 2048, 0.5f);

		RConnection rConnection = new RConnection();

		rConnection.eval("library(tuneR)");
		rConnection.eval("library(seewave)");

		System.out.printf("Window\t | Strongest Freq.\n");
		System.out.println("---------|----------------");

		int i = 0;
		for (int[] currentWindow : windows) {

			rConnection.assign("window", currentWindow);
			rConnection.eval("wave <- Wave(window)");
			rConnection.eval("spec <- meanspec(wave, plot = FALSE)");
			REXP strongestFreq = rConnection.eval("specprop(spec)$mode");
			System.out.printf("Nr. %d\t | %,.3f\n", ++i, strongestFreq.asDouble());
		}
	}

	private static int[] readAudioFileData(String filePath) throws IOException, UnsupportedAudioFileException {

		// File file = new File(filePath);
		//
		// AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
		//
		// int in;
		// ArrayList<Integer> inList = new ArrayList<Integer>();
		// while ((in = inputStream.read()) != -1) {
		// inList.add(in);
		// }
		//
		// int[] samples = new int[inList.size()];
		//
		// for (int i = 0; i < inList.size(); i++) {
		// samples[i] = inList.get(i);
		// }
		//
		// return samples;

		WavAudioFileReader reader = new WavAudioFileReader(filePath);
		return reader.readData();
	}
}
