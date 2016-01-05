package com.audioant.audio.analysis;

import java.io.IOException;
import java.util.Observable;

import org.rosuda.REngine.Rserve.RserveException;

import com.audioant.audio.analysis.features.energy.EnergyMatchAnalyser;
import com.audioant.audio.analysis.features.mfcc.MfccMatchAnalyser;
import com.audioant.audio.analysis.features.spectralRolloffPoint.SrpMatchAnalyser;
import com.audioant.audio.analysis.features.strongestFrequency.FrequnecyMatchAnalyser;
import com.audioant.audio.model.ResultModel;
import com.audioant.audio.model.SoundModel;
import com.audioant.config.Parameters.Audio.Analysis;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class MatchAnalyser extends Observable {

	// private long time = System.currentTimeMillis();

	private SoundModel soundModel;

	private SrpMatchAnalyser srpAnalyser;
	private MfccMatchAnalyser mfccAnalyser;
	private EnergyMatchAnalyser energyAnalyser;
	private FrequnecyMatchAnalyser frequnecyAnalyser;

	public MatchAnalyser(String soundName) throws RserveException, IOException {

		soundModel = new SoundModel(soundName);

		srpAnalyser = new SrpMatchAnalyser(soundModel);
		mfccAnalyser = new MfccMatchAnalyser(soundModel);
		energyAnalyser = new EnergyMatchAnalyser(soundModel);
		frequnecyAnalyser = new FrequnecyMatchAnalyser(soundModel);
	}

	public void addAnalysisResult(ResultModel resultModel) {
		srpAnalyser.addValue(resultModel.getSpectralRolloffPoint());
		mfccAnalyser.addValue(resultModel.getMfcc());
		energyAnalyser.addValue(resultModel.getEnergy());
		frequnecyAnalyser.addValue(resultModel.getStrongestFrequencies());
	}

	protected boolean isMatch() {

		/*
		 * *********************************************************************
		 * FOR TESTING PURPOSE *************************************************
		 * *********************************************************************
		 * if (frequnecyAnalyser.getMatch() >=
		 * Analysis.MATCH_THRESHOLD_STRONGEST_FREQUENCY / 2 &&
		 * mfccAnalyser.getMatch() >= Analysis.MATCH_THRESHOLD_MFCC / 2 &&
		 * srpAnalyser.getMatch() >= Analysis.MATCH_THRESHOLD_SRP / 2 &&
		 * energyAnalyser.getMatch() >= Analysis.MATCH_THRESHOLD_ENERGY / 2) {
		 *
		 * if (System.currentTimeMillis() - time > 350) { System.out.println();
		 * } time = System.currentTimeMillis();
		 *
		 * System.out.printf("%.2f\t%.2f\t%.2f\t%.2f\t",
		 * frequnecyAnalyser.getMatch(), srpAnalyser.getMatch(),
		 * mfccAnalyser.getMatch(), energyAnalyser.getMatch());
		 *
		 * if (frequnecyAnalyser.getMatch() <
		 * Analysis.MATCH_THRESHOLD_STRONGEST_FREQUENCY) {
		 * System.out.print("f"); } if (srpAnalyser.getMatch() <
		 * Analysis.MATCH_THRESHOLD_SRP) { System.out.print("s"); } if
		 * (mfccAnalyser.getMatch() < Analysis.MATCH_THRESHOLD_MFCC) {
		 * System.out.print("m"); } if (energyAnalyser.getMatch() <
		 * Analysis.MATCH_THRESHOLD_ENERGY) { System.out.print("e"); }
		 *
		 * System.out.println(); }
		 */

		return frequnecyAnalyser.getMatch() >= Analysis.MATCH_THRESHOLD_STRONGEST_FREQUENCY
				&& mfccAnalyser.getMatch() >= Analysis.MATCH_THRESHOLD_MFCC
				&& srpAnalyser.getMatch() >= Analysis.MATCH_THRESHOLD_SRP
				&& energyAnalyser.getMatch() >= Analysis.MATCH_THRESHOLD_ENERGY;
	}

	public double getSrpMatch() {
		return srpAnalyser.getMatch();
	}

	public double getFrequencyMatch() {
		return frequnecyAnalyser.getMatch();
	}

	public double getMfccMatch() {
		return mfccAnalyser.getMatch();
	}

	public double getEnergyMatch() {
		return energyAnalyser.getMatch();
	}

	public SoundModel getSoundModel() {
		return soundModel;
	}
}