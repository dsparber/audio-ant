package com.audioant.audio.analysis.match.srp;

import java.io.IOException;
import java.util.ArrayList;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RserveException;

import com.audioant.audio.analysis.match.MatchFunctions;
import com.audioant.audio.model.SoundModel;
import com.audioant.config.Parameters.WorkingDir;
import com.audioant.io.csv.CsvReader;
import com.audioant.tools.MaxSizeArrayList;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class SrpMatchAnalyser {

	protected double[] savedValues;
	protected ArrayList<Double> recentValues;

	private SoundModel soundModel;

	public SrpMatchAnalyser(SoundModel soundModel) throws RserveException, IOException {

		this.soundModel = soundModel;

		savedValues = loadCsvValues();

		recentValues = new MaxSizeArrayList<Double>(savedValues.length);
	}

	public double getMatch() {

		double recentArray[] = new double[recentValues.size()];

		for (int i = 0; i < recentArray.length; i++) {
			recentArray[i] = recentValues.get(i);
		}

		double result = 0;

		try {
			result = Math.abs(MatchFunctions.getPearsonCoefficient(savedValues, recentArray));
		} catch (REngineException | REXPMismatchException e) {
			e.printStackTrace();
		}

		return result;
	}

	private double[] loadCsvValues() throws IOException {

		CsvReader reader = new CsvReader(soundModel.getFolder() + WorkingDir.SRP_CSV);
		String[] csvValues = reader.readSingleCol(0);

		double[] values = new double[csvValues.length];

		for (int i = 0; i < csvValues.length; i++) {

			values[i] = Double.parseDouble(csvValues[i]);
		}

		return values;
	}

	public void addValue(double spectralRolloffPoint) {
		recentValues.add(spectralRolloffPoint);
	}
}