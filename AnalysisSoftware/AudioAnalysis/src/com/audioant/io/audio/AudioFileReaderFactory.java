package com.audioant.io.audio;

import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import com.audioant.config.Config;

public class AudioFileReaderFactory {

	public static AudioFileReader getFileReader(String pathname) throws IOException, UnsupportedAudioFileException {

		if (pathname.matches(Config.AUDIO_FILE_PATTERN_WAV)) {
			return new WavAudioFileReader(pathname);
		} else if (pathname.matches(Config.AUDIO_FILE_PATTERN_MP3)) {
			return new Mp3AudioFileReader(pathname);
		} else {
			return null;
		}

	}

}
