package com.audioant.audio.analysis;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import com.audioant.audio.analysis.sound.strongestFrequency.FrequencyModel;
import com.audioant.audio.analysis.sound.strongestFrequency.StrongestFrequenciesModel;
import com.audioant.config.Config;
import com.audioant.io.r.RConnectionSingleton;

public class WindowAnalyser {

	private RConnection rConnection;

	public WindowAnalyser() {
		rConnection = RConnectionSingleton.getUniqueInstance();
	}

	public void assignSamples(int[] samples, float sampleRate) throws REngineException {
		rConnection.assign("window", samples);
		rConnection.eval("wave <- Wave(window, samp.rate = " + sampleRate + ")");
	}

	public void applyBandpassFilter(double min, double max) throws RserveException {
		rConnection.eval("wave <- Wave(ffilter(wave, from = " + min + ", to = " + max
				+ ", fftw = TRUE), samp.rate = wave@samp.rate, bit = wave@bit)");
	}

	public void generateSpectrum() throws RserveException {
		rConnection.eval("spec <- meanspec(wave, fftw = TRUE, plot = FALSE)");
	}

	public double[] getMFCC() throws RserveException, REXPMismatchException {

		rConnection.eval("mfcc <- colMeans(melfcc(wave))");
		rConnection.eval("mfcc <- mfcc / max(mfcc)");

		return rConnection.eval("mfcc").asDoubles();
	}

	public StrongestFrequenciesModel getStrongestFrequencies() {

		try {
			rConnection.eval("peaks = spec[findPeaks(spec[,2]) -1,]");
			rConnection.eval(
					"strongest = peaks[peaks[,2] >=" + Config.AUDIO_ANALYSIS_FREQUENCY_THRESHOLD_AMPLITUDE + ", ]");

			StrongestFrequenciesModel model = new StrongestFrequenciesModel();

			if (rConnection.eval("length(strongest)").asInteger() > 2) {

				rConnection.eval("ordered = strongest[order(strongest[,2], decreasing = T),]");
				int length = rConnection.eval("length(strongest[,1])").asInteger();

				for (int i = 0; i < length; i++) {

					double frequency = rConnection.eval("ordered[" + (i + 1) + ", 1]*1000").asDouble();
					double amplitude = rConnection.eval("ordered[" + (i + 1) + ", 2]").asDouble();

					model.addFrequency(new FrequencyModel(frequency, amplitude));
				}

			} else {
				double frequency = rConnection.eval("strongest[1]*1000").asDouble();
				double amplitude = rConnection.eval("strongest[2]").asDouble();

				model.addFrequency(new FrequencyModel(frequency, amplitude));
			}

			return model;

		} catch (RserveException | REXPMismatchException e) {
			e.printStackTrace();
			return null;
		}
	}

	public double getRmsEnergy() throws RserveException, REXPMismatchException {
		return rConnection.eval("rms(wave@left)").asDouble();
	}

	public double getEnergy() throws RserveException, REXPMismatchException {
		return rConnection.eval("20*log10(mean(abs(wave@left)))").asDouble();
	}

	public double getSpectralRolloffPoint() throws RserveException, REXPMismatchException {

		rConnection.eval("sum <- sum(spec[,2], na.rm = TRUE)");
		rConnection.eval("current <- 0.0");
		rConnection.eval("index <- 0");

		rConnection.eval("while (current <= (" + Config.AUDIO_ANALYSIS_SRP_BORDER + " * sum)){\n"
				+ "index <- index + 1\n" + "current = current + spec[index, 2]\n" + "}");

		return rConnection.eval("spec[index,1] * 1000").asDouble();
	}
}
