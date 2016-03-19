package com.audioant.audio.analysis.match;

import java.io.IOException;
import java.util.Observable;

import org.rosuda.REngine.Rserve.RserveException;

import com.audioant.audio.analysis.match.energy.EnergyMatchAnalyser;
import com.audioant.audio.analysis.match.mfcc.MfccMatchAnalyser;
import com.audioant.audio.analysis.match.srp.SrpMatchAnalyser;
import com.audioant.audio.analysis.match.strongestFrequency.FrequnecyMatchAnalyser;
import com.audioant.audio.model.MatchResult;
import com.audioant.audio.model.Result;
import com.audioant.audio.model.Sound;
import com.audioant.config.Config;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class MatchAnalyser extends Observable {

	private Sound sound;

	private SrpMatchAnalyser srpAnalyser;
	private MfccMatchAnalyser mfccAnalyser;
	private EnergyMatchAnalyser energyAnalyser;
	private FrequnecyMatchAnalyser frequnecyAnalyser;

	public MatchAnalyser(Sound sound) throws RserveException, IOException {
		this.sound = sound;

		srpAnalyser = new SrpMatchAnalyser(sound);
		mfccAnalyser = new MfccMatchAnalyser(sound);
		energyAnalyser = new EnergyMatchAnalyser(sound);
		frequnecyAnalyser = new FrequnecyMatchAnalyser(sound);
	}

	public void addAnalysisResult(Result result) {
		if (result != null) {
			srpAnalyser.addValue(result.getSpectralRolloffPoint());
			mfccAnalyser.addValue(result.getMfcc());
			energyAnalyser.addValue(result.getEnergy());
			frequnecyAnalyser.addValue(result.getStrongestFrequencies());
		} else {
			srpAnalyser.addValue(-1);
			mfccAnalyser.addValue(null);
			energyAnalyser.addValue(-1);
			frequnecyAnalyser.addValue(null);
		}
	}

	private boolean isMatch() {
		return isMinThresholdReached() && isMatchByWeightedSum();
	}

	private boolean isSureMatch() {
		return isMatchByWeightedSum() && isMatchByThresholds();
	}

	private double getSrpMatch() {
		return srpAnalyser.getMatch();
	}

	private double getFrequencyMatch() {
		return frequnecyAnalyser.getMatch();
	}

	private double getMfccMatch() {
		return mfccAnalyser.getMatch();
	}

	private double getEnergyMatch() {
		return energyAnalyser.getMatch();
	}

	private Sound getSoundModel() {
		return sound;
	}

	private double getWeightedSum() {
		return (frequnecyAnalyser.getMatch() * Config.AUDIO_ANALYSIS_MATCH_WEIGHT_FREQUENCY
				+ srpAnalyser.getMatch() * Config.AUDIO_ANALYSIS_MATCH_WEIGHT_SRP
				+ mfccAnalyser.getMatch() * Config.AUDIO_ANALYSIS_MATCH_WEIGHT_MFCC
				+ energyAnalyser.getMatch() * Config.AUDIO_ANALYSIS_MATCH_WEIGHT_ENERGY)
				/ Config.AUDIO_ANALYSIS_MATCH_WEIGHT_THRESHOLD;
	}

	private boolean isMatchByWeightedSum() {
		return getWeightedSum() >= 1;
	}

	private boolean isMatchByThresholds() {
		return frequnecyAnalyser.getMatch() >= Config.AUDIO_ANALYSIS_MATCH_THRESHOLD_FREQUENCY
				&& mfccAnalyser.getMatch() >= Config.AUDIO_ANALYSIS_MATCH_THRESHOLD_MFCC
				&& srpAnalyser.getMatch() >= Config.AUDIO_ANALYSIS_MATCH_THRESHOLD_SRP
				&& energyAnalyser.getMatch() >= Config.AUDIO_ANALYSIS_MATCH_THRESHOLD_ENERGY;
	}

	private boolean isMinThresholdReached() {
		return frequnecyAnalyser.getMatch() >= Config.AUDIO_ANALYSIS_MATCH_THRESHOLD_MIN_FREQUENCY
				&& mfccAnalyser.getMatch() >= Config.AUDIO_ANALYSIS_MATCH_THRESHOLD_MIN_MFCC
				&& srpAnalyser.getMatch() >= Config.AUDIO_ANALYSIS_MATCH_THRESHOLD_MIN_SRP
				&& energyAnalyser.getMatch() >= Config.AUDIO_ANALYSIS_MATCH_THRESHOLD_MIN_ENERGY;
	}

	public MatchResult getMatchResult() {
		return new MatchResult(isMatch(), isSureMatch(), isMatchByWeightedSum(), isMatchByThresholds(),
				isMinThresholdReached(), getWeightedSum(), getFrequencyMatch(), getEnergyMatch(), getMfccMatch(),
				getSrpMatch(), getSoundModel());
	}
}