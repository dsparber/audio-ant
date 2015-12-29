package com.audioant.audio.analysis;

import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RserveException;

import com.audioant.audio.windowing.Windowing;
import com.audioant.config.Parameters.Audio;
import com.audioant.io.audio.AudioFileReader;
import com.audioant.io.audio.AudioFileReaderFactory;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class SoundFileAnalyser extends SoundAnalyser {

	private String pathname;

	private boolean match;
	private double frequencyMatch, srpMatch;

	public SoundFileAnalyser(String soundName, String pathname) throws RserveException, IOException {
		super(soundName);
		this.pathname = pathname;
	}

	public void analyse() throws IOException, REngineException, REXPMismatchException, UnsupportedAudioFileException {

		AudioFileReader reader = AudioFileReaderFactory.getFileReader(pathname);

		float sampleRate = reader.getSampleRate();

		int[][] windows = Windowing.createWindows(reader.readData(), Audio.WINDOW_SIZE, 0f);

		boolean match = false;
		double maxSrp = 0;
		double maxFreq = 0;

		for (int[] samples : windows) {

			addSamples(samples, sampleRate);

			boolean currentMatch = isMatch();
			double currentFreqMatch = getfrequencyMatch();
			double currentSrpMatch = getSrpMatch();

			if (currentMatch) {
				match = true;
			}

			if (currentFreqMatch > maxFreq) {
				maxFreq = currentFreqMatch;
			}

			if (currentSrpMatch > maxSrp) {
				maxSrp = currentSrpMatch;
			}
		}
		this.match = match;
		frequencyMatch = maxFreq;
		srpMatch = maxSrp;
	}

	public boolean getMatch() {
		return match;
	}

	public double getMaxFrequnecyMatch() {
		return frequencyMatch;
	}

	public double getMaxSrpMatch() {
		return srpMatch;
	}
}
