package com.audioant.audio.analysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RserveException;

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

	private List<SoundAnalyser> soundAnalysers;

	public AudioAnalyser() throws RserveException, IOException {

		soundAnalysers = new ArrayList<SoundAnalyser>();

		File[] learnedSounds = new File(WorkingDir.FOLDER_LEARNED_SOUNDS).listFiles();

		for (File file : learnedSounds) {

			if (file.isDirectory()) {

				String soundName = file.getName();

				soundAnalysers.add(new SoundAnalyser(soundName));
			}
		}
	}

	protected void addSamples(int[] samples, float sampleRate) throws REngineException, REXPMismatchException {

		for (SoundAnalyser analyser : soundAnalysers) {
			analyser.addSamples(samples, sampleRate);
		}
	}

	protected List<SoundModel> getMatches() {

		List<SoundModel> sounds = new ArrayList<SoundModel>();

		for (SoundAnalyser analyser : soundAnalysers) {
			if (analyser.isMatch()) {
				sounds.add(analyser.getSoundModel());
			}
		}

		return sounds;
	}
}