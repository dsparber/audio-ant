package io.audio;

import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioFileReaderFactory {

	public static AudioFileReader getFileReader(String pathname) throws IOException, UnsupportedAudioFileException {

		if (pathname.endsWith(".wav")) {
			return new WavAudioFileReader(pathname);
		} else if (pathname.endsWith(".mp3")) {
			return new Mp3AudioFileReader(pathname);
		} else {
			return null;
		}

	}

}
