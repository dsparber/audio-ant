package com.audioant.audio.analysis;

import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RserveException;

import com.audioant.audio.analysis.match.MatchAnalyser;
import com.audioant.audio.analysis.sound.SoundAnalyser;
import com.audioant.audio.model.MatchResult;
import com.audioant.audio.model.Result;
import com.audioant.audio.model.Sound;
import com.audioant.audio.windowing.Windowing;
import com.audioant.config.Config;
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
	private double frequencyMatch, srpMatch, mfccMatch, energyMatch;
	private MatchAnalyser matchAnalyser;

	public SoundFileAnalyser(String pathname) throws RserveException, IOException {
		super();
		this.pathname = pathname;
		matchAnalyser = new MatchAnalyser(new Sound());
	}

	public void analyse() throws IOException, REngineException, REXPMismatchException, UnsupportedAudioFileException {

		AudioFileReader reader = AudioFileReaderFactory.getFileReader(pathname);

		float sampleRate = reader.getSampleRate();

		int[][] windows = Windowing.createWindows(reader.readData(), Config.AUDIO_WINDOW_SIZE, 0f);

		boolean match = false;
		double maxSrp = 0;
		double maxFreq = 0;
		double maxMfcc = 0;
		double maxEnergy = 0;

		for (int[] samples : windows) {

			Result resultModel = analyseSamples(samples, sampleRate);

			matchAnalyser.addAnalysisResult(resultModel);

			MatchResult matchResult = matchAnalyser.getMatchResult();

			boolean currentMatch = matchResult.isMatch();
			double currentFreqMatch = matchResult.getFrequencyMatch();
			double currentSrpMatch = matchResult.getSrpMatch();
			double currentMfccMatch = matchResult.getMfccMatch();
			double currentEnergy = matchResult.getEnergyMatch();

			if (currentMatch) {
				match = true;
			}

			if (currentFreqMatch > maxFreq) {
				maxFreq = currentFreqMatch;
			}

			if (currentSrpMatch > maxSrp) {
				maxSrp = currentSrpMatch;
			}

			if (currentMfccMatch > maxMfcc) {
				maxMfcc = currentMfccMatch;
			}

			if (currentEnergy > maxEnergy) {
				maxEnergy = currentEnergy;
			}
		}
		this.match = match;
		frequencyMatch = maxFreq;
		srpMatch = maxSrp;
		mfccMatch = maxMfcc;
		energyMatch = maxEnergy;
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

	public double getMaxMfccMatch() {
		return mfccMatch;
	}

	public double getMaxEnergyMatch() {
		return energyMatch;
	}

}
