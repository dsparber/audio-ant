package com.audioant.audio.analysis;

import java.io.IOException;
import java.util.Observable;

import org.rosuda.REngine.Rserve.RserveException;

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

	private SoundModel soundModel;

	private SrpMatchAnalyser srpAnalyser;
	private FrequnecyMatchAnalyser frequnecyAnalyser;

	public MatchAnalyser(String soundName) throws RserveException, IOException {

		soundModel = new SoundModel(soundName);

		srpAnalyser = new SrpMatchAnalyser(soundModel);
		frequnecyAnalyser = new FrequnecyMatchAnalyser(soundModel);
	}

	public void addAnalysisResult(ResultModel resultModel) {
		srpAnalyser.addValue(resultModel.getSpectralRolloffPoint());
		frequnecyAnalyser.addValue(resultModel.getStrongestFrequencies());

	}

	protected boolean isMatch() {

		if (frequnecyAnalyser.getMatch() > Analysis.STRONGEST_FREQUENCY_MATCH_THRESHOLD) {
			if (srpAnalyser.getMatch() > Analysis.SRP_MATCH_THRESHOLD) {
				return true;
			}
		}
		return false;
	}

	public double getSrpMatch() {
		return srpAnalyser.getMatch();
	}

	public double getfrequencyMatch() {
		return frequnecyAnalyser.getMatch();
	}

	public SoundModel getSoundModel() {
		return soundModel;
	}
}