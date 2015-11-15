import java.io.IOException;

import wave.WavAudioFileReader;
import windowing.Windowing;

public class AudioAnalysis {

	public static void main(String[] args) throws IOException {

		Integer[] audioSignal = readAudioFileData("../../Audio Testfiles/Doorbell_d01.wav");

		Integer[][] windows = Windowing.generateHannWindows(audioSignal, 512, 0.5f);

	}

	private static Integer[] readAudioFileData(String filePath) throws IOException {

		WavAudioFileReader reader = new WavAudioFileReader(filePath);

		return reader.readData();
	}
}
