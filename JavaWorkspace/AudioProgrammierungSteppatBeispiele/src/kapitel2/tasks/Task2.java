package kapitel2.tasks;

import java.io.IOException;

import kapitel2.res.WavAudioFile;

public class Task2 {

	public static void main(String[] args) throws IOException {
		WavAudioFile file = new WavAudioFile("out/task2/1638.wav");
		file.createAudioData(1638, 440, 0, 88200);

		file = new WavAudioFile("out/task2/3276.wav");
		file.createAudioData(3276, 440, 0, 88200);

		file = new WavAudioFile("out/task2/8190.wav");
		file.createAudioData(8190, 440, 0, 88200);

		file = new WavAudioFile("out/task2/16384.wav");
		file.createAudioData(16384, 440, 0, 88200);

		file = new WavAudioFile("out/task2/32767.wav");
		file.createAudioData(32767, 440, 0, 88200);

	}
}
