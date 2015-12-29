package com.audioant.audio.learning.features.spectralRolloffPoint;

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

public class SrpLearner {

	protected String pathnameOut;

	private WindowAnalyser analyser;

	private List<Double> results;

	public SrpLearner(WindowAnalyser analyser, String pathname) {
		this.analyser = analyser;

		pathnameOut = pathname + WorkingDir.SRP_CSV;
		results = new ArrayList<Double>();
	}

	public void saveFeatures() throws LineUnavailableException, IOException, REngineException, REXPMismatchException,
			UnsupportedAudioFileException {

		String[] out = new String[results.size()];

		for (int i = 0; i < out.length; i++) {
			out[i] = results.get(i).toString();
		}

		CsvWriter writer = new CsvWriter(pathnameOut);
		writer.writeSingleColmn(out);
	}

	public boolean analyseWindow() throws REngineException, REXPMismatchException {

		double srp = analyser.getSpectralRolloffPoint();

		results.add(srp);

		return true;
	}
}
