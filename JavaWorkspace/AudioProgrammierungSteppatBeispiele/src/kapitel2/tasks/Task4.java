package kapitel2.tasks;

import java.io.IOException;

import kapitel2.res.WavAudioFile;

public class Task4 {

	public static void main(String[] args) throws IOException {
		WavAudioFile file = new WavAudioFile("out/task4/22050Hz.wav");
		file.createAudioData(32767, 22050, 0, 88200);

		file = new WavAudioFile("out/task4/43100Hz.wav");
		file.createAudioData(32767, 43100, 0, 88200);

		file = new WavAudioFile("out/task4/43600Hz.wav");
		file.createAudioData(32767, 43600, 0, 88200);
	}
}
