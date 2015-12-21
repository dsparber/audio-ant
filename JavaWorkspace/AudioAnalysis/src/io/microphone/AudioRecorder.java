package io.microphone;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import config.Parameters.Audio;

/**
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */

public class AudioRecorder {
	private AudioFormat audioFormat;
	private TargetDataLine targetDataLine;

	private File audioFile;

	public AudioRecorder(String outputPath) {
		audioFile = new File(outputPath);
		audioFile.getParentFile().mkdirs();
	}

	public void startCapturing() throws LineUnavailableException {
		audioFormat = Audio.AUDIO_FORMAT;

		Info dataLineInfo = new Info(TargetDataLine.class, audioFormat);

		targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);

		new CaptureThread().start();

	}

	public void stopCapture() {
		targetDataLine.stop();
		targetDataLine.close();
	}

	private class CaptureThread extends Thread {
		@Override
		public void run() {
			Type fileType = Audio.FILE_TYPE;

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
