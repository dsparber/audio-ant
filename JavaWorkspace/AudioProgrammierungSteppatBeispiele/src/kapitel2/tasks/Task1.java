package kapitel2.tasks;

import java.io.IOException;

import kapitel2.res.WavAudioFile;

public class Task1 {

	public static void main(String[] args) throws IOException {
		WavAudioFile file = new WavAudioFile("out/task1/440Hz.wav");
		file.createAudioData(32767, 440, 0, 88200);

		file = new WavAudioFile("out/task1/880Hz.wav");
		file.createAudioData(32767, 880, 0, 88200);

		file = new WavAudioFile("out/task1/1320Hz.wav");
		file.createAudioData(32767, 1320, 0, 88200);

		file = new WavAudioFile("out/task1/1760Hz.wav");
		file.createAudioData(32767, 1760, 0, 88200);

	}
}
