package audio.analysis;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import singleton.RConnectionSingletone;

public class WindowAnalyser {

	private RConnection rConnection;

	public WindowAnalyser() {
		rConnection = RConnectionSingletone.getUniqueInstance();
	}

	public void assignSamples(int[] samples, float sampleRate) throws REngineException {
		rConnection.assign("window", samples);
		rConnection.eval("wave <- Wave(window, samp.rate = " + sampleRate + ")");
	}

	public double getStrongestFrequency() throws RserveException, REXPMismatchException {

		rConnection.eval("spec <- meanspec(wave, plot = FALSE)");

		return rConnection.eval("specprop(spec)$mode").asDouble();
	}

}
