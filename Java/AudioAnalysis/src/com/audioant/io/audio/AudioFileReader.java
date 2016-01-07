package com.audioant.io.audio;

import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public interface AudioFileReader {

	int[] readData() throws IOException, UnsupportedAudioFileException;

	float getSampleRate();
}