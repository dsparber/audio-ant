package kapitel2.tasks;

import java.io.IOException;

import kapitel2.res.WavAudioFile;

public class Task3 {

	public static void main(String[] args) throws IOException {
		WavAudioFile file = new WavAudioFile("out/task3/0.wav");
		file.createAudioData(32767, 440, 0, 88200);

		file = new WavAudioFile("out/task3/90.wav");
		file.createAudioData(32767, 440, 90, 88200);

		file = new WavAudioFile("out/task3/180.wav");
		file.createAudioData(32767, 440, 180, 88200);

		file = new WavAudioFile("out/task3/270.wav");
		file.createAudioData(32767, 440, 270, 88200);
	}
}
