package singleton;

import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class RConnectionSingletone {

	/** unique instance */
	private static RConnection instance = null;

	/**
	 * Get the unique instance of this class.
	 */
	public static synchronized RConnection getUniqueInstance() {

		if (instance == null) {
			try {
				instance = new RConnection();
				instance.eval("library(tuneR)");
				instance.eval("library(seewave)");
			} catch (RserveException e) {
				e.printStackTrace();
			}
		}

		return instance;

	}
}
