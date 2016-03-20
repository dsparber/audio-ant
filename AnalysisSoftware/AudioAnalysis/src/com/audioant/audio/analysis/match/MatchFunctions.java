package com.audioant.audio.analysis.match;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;

import com.audioant.io.r.RConnectionSingleton;

public class MatchFunctions {

	public static double getPearsonCoefficient(final double[] d1, final double[] d2)
			throws REngineException, REXPMismatchException {

		RConnection rConnection = RConnectionSingleton.getUniqueInstance();

		rConnection.assign("v.x", d1);
		rConnection.assign("v.y", d2);

		rConnection.eval("n <- length(v.x)");

		rConnection.eval("x.mean = mean(v.x)");
		rConnection.eval("y.mean = mean(v.y)");

		rConnection.eval("s.x = sqrt(1/(n-1)*sum((v.x - x.mean)^2))");
		rConnection.eval("s.y = sqrt(1/(n-1)*sum((v.y - y.mean)^2))");
		rConnection.eval("s.xy = 1/(n-1)*sum((v.x - x.mean)*(v.y - y.mean))");

		return rConnection.eval("s.xy / (s.x*s.y)").asDouble();
	}
}
