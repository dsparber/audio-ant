package singleton;

import java.io.IOException;

import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class RConnectionSingleton {

	/** unique instance */
	private static RConnection instance = null;

	/**
	 * Get the unique instance of this class.
	 */
	public static synchronized RConnection getUniqueInstance() {

		if (instance == null) {

			try {
				Runtime.getRuntime().exec("R CMD Rserve");
			} catch (IOException e) {
				System.err.println("Rserve could not be started");
				e.printStackTrace();
			}

			try {
				instance = new RConnection();
				instance.eval("library(tuneR)");
				instance.eval("library(seewave)");
			} catch (RserveException e) {
				System.err.println("Rserve is not running");
			}
		}
		return instance;
	}
}
