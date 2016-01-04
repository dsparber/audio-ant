package com.audioant.audio.learning.features.mfcc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;

import com.audioant.audio.analysis.WindowAnalyser;
import com.audioant.config.Parameters.WorkingDir;
import com.audioant.io.csv.CsvWriter;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */

public class MfccLearner {

	protected String pathnameOut;

	private WindowAnalyser analyser;

	private List<double[]> results;

	public MfccLearner(WindowAnalyser analyser, String pathname) {
		this.analyser = analyser;

		pathnameOut = pathname + WorkingDir.MFCC_CSV;
		results = new ArrayList<double[]>();
	}

	public void saveFeatures() throws LineUnavailableException, IOException, REngineException, REXPMismatchException,
			UnsupportedAudioFileException {

		String[][] out = new String[results.size()][results.get(0).length];

		for (int i = 0; i < out.length; i++) {
			for (int j = 0; j < out[i].length; j++) {
				out[i][j] = Double.toString(results.get(i)[j]);
			}
		}

		CsvWriter writer = new CsvWriter(pathnameOut);
		writer.writeMatrix(out);
	}

	public boolean analyseWindow() throws REngineException, REXPMismatchException {

		double[] mfcc = analyser.getMFCC();

		results.add(mfcc);

		return true;
	}
}
