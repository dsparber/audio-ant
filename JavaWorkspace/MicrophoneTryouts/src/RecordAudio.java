import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

/**
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */

public class RecordAudio {
	AudioFormat audioFormat;
	TargetDataLine targetDataLine;

	public void captureAudio() throws LineUnavailableException {
		audioFormat = getAudioFormat();

		Info dataLineInfo = new Info(TargetDataLine.class, audioFormat);

		targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);

		new CaptureThread().start();

	}

	private AudioFormat getAudioFormat() {

		float sampleRate = 16000;
		int sampleSizeInBits = 16;
		int channels = 1; // mono audio, 2 = stereo
		boolean bigEndian = false;
		boolean signed = true;

		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}

	public void stopCapture() {
		targetDataLine.stop();
		targetDataLine.close();
	}

	private class CaptureThread extends Thread {
		@Override
		public void run() {
			Type fileType = Type.WAVE;
			File audioFile = new File("out/recording.wav");

			try {
				targetDataLine.open(audioFormat);
				targetDataLine.start();
				AudioSystem.write(new AudioInputStream(targetDataLine), fileType, audioFile);

			} catch (LineUnavailableException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
