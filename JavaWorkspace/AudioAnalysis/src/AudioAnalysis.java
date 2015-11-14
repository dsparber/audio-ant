import java.io.IOException;

import wave.WavAudioFileReader;

public class AudioAnalysis {

	public static void main(String[] args) throws IOException {

		Integer[] audioSignal = readAudioFileData("../../Audio Testfiles/Doorbell_d01.wav");

	}

	private static Integer[] readAudioFileData(String filePath) throws IOException {

		WavAudioFileReader reader = new WavAudioFileReader(filePath);

		return reader.readData();
	}
}
