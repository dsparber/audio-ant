package com.audioant.io.audio;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.audioant.tools.LittleEndian;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class Mp3AudioFileReader implements AudioFileReader {

	private AudioFormat format;
	private File file;

	public Mp3AudioFileReader(String pathname) throws UnsupportedAudioFileException, IOException {

		file = new File(pathname);
		AudioInputStream in = AudioSystem.getAudioInputStream(file);
		AudioFormat undecodedFormat = in.getFormat();

		format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, undecodedFormat.getSampleRate(), 16,
				undecodedFormat.getChannels(), undecodedFormat.getChannels() * 2, undecodedFormat.getSampleRate(),
				false);

	}

	@Override
	public int[] readData() throws IOException, UnsupportedAudioFileException {
		AudioInputStream in = AudioSystem.getAudioInputStream(format, AudioSystem.getAudioInputStream(file));

		ArrayList<Integer> samples = new ArrayList<Integer>();

		byte[] inBytes = new byte[format.getSampleSizeInBits() / 8];

		while (in.read(inBytes) > 0) {
			int sample;
			if (in.getFormat().getFrameSize() == 2) {
				sample = LittleEndian.toShort(inBytes);
			} else {
				sample = LittleEndian.toInt(inBytes);
			}
			samples.add(sample);
		}

		int[] result = new int[samples.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = samples.get(i);
		}
		return result;
	}

	@Override
	public float getSampleRate() {
		return format.getSampleRate();
	}

}
