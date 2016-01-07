package com.audioant.io.audio;

import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import com.audioant.config.Parameters.Audio;

public class AudioFileReaderFactory {

	public static AudioFileReader getFileReader(String pathname) throws IOException, UnsupportedAudioFileException {

		if (pathname.endsWith(Audio.WAV_FILEPATTERN)) {
			return new WavAudioFileReader(pathname);
		} else if (pathname.endsWith(Audio.MP3_FILEPATTERN)) {
			return new Mp3AudioFileReader(pathname);
		} else {
			return null;
		}

	}

}
