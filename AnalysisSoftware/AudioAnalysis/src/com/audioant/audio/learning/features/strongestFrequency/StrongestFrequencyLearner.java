package com.audioant.audio.learning.features.strongestFrequency;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;

import com.audioant.audio.analysis.WindowAnalyser;
import com.audioant.audio.analysis.sound.strongestFrequency.StrongestFrequenciesModel;
import com.audioant.config.Parameters.Audio.Analysis;
import com.audioant.config.Parameters.WorkingDir;
import com.audioant.io.csv.CsvWriter;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */

public class StrongestFrequencyLearner {

	protected String pathnameOut;

	private WindowAnalyser analyser;
	private List<StrongestFrequenciesModel> results;

	public StrongestFrequencyLearner(WindowAnalyser analyser, String pathname) {
		this.analyser = analyser;

		pathnameOut = pathname + WorkingDir.FREQUENCIES_CSV;
		results = new ArrayList<StrongestFrequenciesModel>();
	}

	public void saveFeatures() throws LineUnavailableException, IOException, REngineException, REXPMismatchException,
			UnsupportedAudioFileException {

		int maxWidth = 0;

		for (StrongestFrequenciesModel model : results) {
			if (model.size() > maxWidth) {
				maxWidth = model.size();
			}
		}

		String[][] out = new String[results.size()][maxWidth];

		for (int i = 0; i < out.length; i++) {
			for (int j = 0; j < out[i].length; j++) {

				if (results.get(i).size() > j) {
					out[i][j] = results.get(i).get(j).toString();
				} else {
					out[i][j] = "";
				}
			}
		}

		CsvWriter writer = new CsvWriter(pathnameOut);
		writer.writeMatrix(out);
	}

	public boolean shouldBeAnalysed() {
		StrongestFrequenciesModel strongestFreq = analyser.getStrongestFrequencies();

		return strongestFreq.size() > 0 && strongestFreq.size() <= Analysis.MAX_PEAK_COUNT;
	}

	public void analyseWindow() throws REngineException, REXPMismatchException {

		StrongestFrequenciesModel strongestFreq = analyser.getStrongestFrequencies();

		results.add(strongestFreq);
	}
}
