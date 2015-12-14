package audio.analysis;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import singleton.RConnectionSingleton;

public class WindowAnalyser {

	private RConnection rConnection;

	public WindowAnalyser() {
		rConnection = RConnectionSingleton.getUniqueInstance();
	}

	public void assignSamples(int[] samples, float sampleRate) throws REngineException {
		rConnection.assign("window", samples);
		rConnection.eval("wave <- Wave(window, samp.rate = " + sampleRate + ")");
	}

	public double getStrongestFrequency() {

		try {
			rConnection.eval("spec <- meanspec(wave, plot = FALSE)");
			return rConnection.eval("specprop(spec)$mode").asDouble();

		} catch (RserveException | REXPMismatchException e) {
			return -1;
		}
	}

	public double getEnergy() throws RserveException, REXPMismatchException {
		return rConnection.eval("20*log10(mean(abs(wave@left)))").asDouble();
	}

}
