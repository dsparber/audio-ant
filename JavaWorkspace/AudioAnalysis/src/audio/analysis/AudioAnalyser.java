package audio.analysis;

import java.io.IOException;
import java.util.Observable;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RserveException;

import audio.analysis.features.spectralRolloffPoint.SrpAnalyser;
import audio.analysis.features.strongestFrequency.FrequnecyAnalyser;
import config.Parameters.Audio.Analysis;
import io.microphone.AudioStreamReader;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class AudioAnalyser extends Observable {

	protected AudioStreamReader reader;

	private WindowAnalyser analyser;
	private FrequnecyAnalyser frequnecyAnalyser;
	private SrpAnalyser srpAnalyser;

	public AudioAnalyser() throws RserveException, IOException {

		analyser = new WindowAnalyser();

		frequnecyAnalyser = new FrequnecyAnalyser(analyser);
		srpAnalyser = new SrpAnalyser(analyser);
	}

	protected void addSamples(int[] samples, float sampleRate) throws REngineException, REXPMismatchException {

		analyser.assignSamples(samples, sampleRate);
		analyser.generateSpectrum();

		frequnecyAnalyser.analyseSamples();
		srpAnalyser.analyseSamples();
	}

	protected double getMatch() {

		if (frequnecyAnalyser.getMatch() > Analysis.STRONGEST_FREQUENCY_MATCH_THRESHOLD) {
			if (srpAnalyser.getMatch() > Analysis.SRP_MATCH_THRESHOLD) {
				return 1;
			}
		}
		return 0;
	}

	public double getSrpMatch() {
		return srpAnalyser.getMatch();
	}

	public double getfrequencyMatch() {
		return frequnecyAnalyser.getMatch();
	}
}