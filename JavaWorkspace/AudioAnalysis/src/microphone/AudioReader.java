package microphone;

import java.io.IOException;
import java.util.Observable;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import parameters.AudioParamters;
import tools.LittleEndian;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class AudioReader extends Observable {

	private TargetDataLine line;
	private AudioInputStream stream;

	private AudioFormat format;
	private DataLine.Info info;

	private StreamThread thread;

	public AudioReader() {

		format = AudioParamters.AUDIO_FORMAT;
		info = new DataLine.Info(TargetDataLine.class, format);

		try {
			line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(format);
		} catch (LineUnavailableException ex) {
			ex.printStackTrace();
		}
	}

	public void streamWindows() {

		thread = new StreamThread();
		thread.start();

	}

	public void stop() {
		line.stop();
		line.close();
	}

	private class StreamThread extends Thread {

		@Override
		public void run() {
			try {
				line.start();
				stream = new AudioInputStream(line);

				byte[] inBytes = new byte[line.getFormat().getSampleSizeInBits() / 8];

				int[] windows = new int[AudioParamters.WINDOW_SIZE];

				int i = 0;
				while (stream.read(inBytes) > 0) {
					int sample;
					if (line.getFormat().getFrameSize() == 2) {
						sample = LittleEndian.toShort(inBytes);
					} else {
						sample = LittleEndian.toInt(inBytes);
					}
					windows[i++] = sample;

					if (i == windows.length) {
						setChanged();
						notifyObservers(windows);
						i = 0;
					}
				}
				stream.close();

			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}

	}

}
