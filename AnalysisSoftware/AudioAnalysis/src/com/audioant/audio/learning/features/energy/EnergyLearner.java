package com.audioant.audio.learning.features.energy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RserveException;

import com.audioant.audio.analysis.WindowAnalyser;
import com.audioant.config.Config;
import com.audioant.io.csv.CsvWriter;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */

public class EnergyLearner {

	protected String pathnameOut;

	private WindowAnalyser analyser;

	private List<Double> results;

	public EnergyLearner(WindowAnalyser analyser, String pathname) {
		this.analyser = analyser;

		pathnameOut = pathname + Config.LEARNED_SOUNDS_FILE_POWER;
		results = new ArrayList<Double>();
	}

	public void saveFeatures() throws LineUnavailableException, IOException, REngineException, REXPMismatchException,
			UnsupportedAudioFileException {

		double max = 0;
		for (Double d : results) {
			if (d > max) {
				max = d;
			}
		}
		for (int i = 0; i < results.size(); i++) {
			results.set(i, (results.get(i) / max));
		}

		String[] out = new String[results.size()];

		for (int i = 0; i < out.length; i++) {
			out[i] = results.get(i).toString();
		}

		CsvWriter writer = new CsvWriter(pathnameOut);
		writer.writeSingleColmn(out);
	}

	public boolean shouldBeAnalysed() throws RserveException, REXPMismatchException {
		return analyser.getRmsEnergy() >= Config.AUDIO_ANALYSIS_POWER_MIN_RMS;
	}

	public void analyseWindow() throws REngineException, REXPMismatchException {

		double energy = analyser.getRmsEnergy();
		results.add(energy);
	}
}
