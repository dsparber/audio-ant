package com.audioant.audio.analysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RserveException;

import com.audioant.audio.model.ResultModel;
import com.audioant.audio.model.SoundModel;
import com.audioant.config.Parameters.WorkingDir;
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

		matchAnalysers = new ArrayList<MatchAnalyser>();

		File[] learnedSounds = new File(WorkingDir.FOLDER_LEARNED_SOUNDS).listFiles();

		for (File file : learnedSounds) {
			if (file.isDirectory()) {

				String soundName = file.getName();
				matchAnalysers.add(new MatchAnalyser(soundName));
			}
		}
	}

	protected void addSamples(int[] samples, float sampleRate) throws REngineException, REXPMismatchException {

		ResultModel resultModel = soundAnalyser.analyseSamples(samples, sampleRate);

		for (MatchAnalyser analyser : matchAnalysers) {
			analyser.addAnalysisResult(resultModel);
		}
	}

	protected List<SoundModel> getMatches() {

		List<SoundModel> sounds = new ArrayList<SoundModel>();

		for (MatchAnalyser analyser : matchAnalysers) {
			if (analyser.isMatch()) {
				sounds.add(analyser.getSoundModel());
			}
		}

		return sounds;
	}
}