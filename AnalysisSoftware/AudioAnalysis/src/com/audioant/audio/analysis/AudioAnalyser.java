package com.audioant.audio.analysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RserveException;

import com.audioant.audio.analysis.match.MatchAnalyser;
import com.audioant.audio.analysis.sound.SoundAnalyser;
import com.audioant.audio.learning.LearnedSounds;
import com.audioant.audio.model.Result;
import com.audioant.audio.model.Sound;
import com.audioant.io.microphone.AudioStreamReader;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class AudioAnalyser extends Observable {

	protected AudioStreamReader reader;

	private SoundAnalyser soundAnalyser;
	private List<MatchAnalyser> matchAnalysers;

	public AudioAnalyser() throws RserveException, IOException {

		soundAnalyser = new SoundAnalyser();

		reloadLearnedSounds();
	}

	protected void addSamples(int[] samples, float sampleRate) throws REngineException, REXPMismatchException {

		Result resultModel = soundAnalyser.analyseSamples(samples, sampleRate);

		for (MatchAnalyser analyser : matchAnalysers) {
			analyser.addAnalysisResult(resultModel);
		}
	}

	protected List<Sound> getMatches() {

		List<Sound> sounds = new ArrayList<Sound>();

		for (MatchAnalyser analyser : matchAnalysers) {
			if (analyser.isMatch()) {
				sounds.add(analyser.getSoundModel());
			}
		}

		return sounds;
	}

	public void reloadLearnedSounds() throws RserveException, IOException {
		matchAnalysers = null;
		matchAnalysers = new ArrayList<MatchAnalyser>();

		for (Sound soundModel : LearnedSounds.getSounds()) {
			matchAnalysers.add(new MatchAnalyser(soundModel));
		}
	}
}