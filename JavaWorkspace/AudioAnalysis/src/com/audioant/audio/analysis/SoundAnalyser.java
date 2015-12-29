package com.audioant.audio.analysis;

import java.io.IOException;
import java.util.Observable;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RserveException;

import com.audioant.audio.analysis.features.spectralRolloffPoint.SrpAnalyser;
import com.audioant.audio.analysis.features.strongestFrequency.FrequnecyAnalyser;
import com.audioant.audio.model.SoundModel;
import com.audioant.config.Parameters.Audio.Analysis;
import com.audioant.io.microphone.AudioStreamReader;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class SoundAnalyser extends Observable {

	protected AudioStreamReader reader;

	private WindowAnalyser analyser;
	private FrequnecyAnalyser frequnecyAnalyser;
	private SrpAnalyser srpAnalyser;

	private SoundModel soundModel;

	public SoundAnalyser(String soundName) throws RserveException, IOException {

		analyser = new WindowAnalyser();

		soundModel = new SoundModel(soundName);

		frequnecyAnalyser = new FrequnecyAnalyser(analyser, soundModel);
		srpAnalyser = new SrpAnalyser(analyser, soundModel);
	}

	protected void addSamples(int[] samples, float sampleRate) throws REngineException, REXPMismatchException {

		analyser.assignSamples(samples, sampleRate);
		analyser.generateSpectrum();

		frequnecyAnalyser.analyseSamples();
		srpAnalyser.analyseSamples();
	}

	protected boolean isMatch() {

		if (frequnecyAnalyser.getMatch() > Analysis.STRONGEST_FREQUENCY_MATCH_THRESHOLD) {
			if (srpAnalyser.getMatch() > Analysis.SRP_MATCH_THRESHOLD) {
				return true;
			}
		}
		return false;
	}

	public double getSrpMatch() {
		return srpAnalyser.getMatch();
	}

	public double getfrequencyMatch() {
		return frequnecyAnalyser.getMatch();
	}

	public SoundModel getSoundModel() {
		return soundModel;
	}
}