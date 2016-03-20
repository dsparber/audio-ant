package com.audioant.io.r;

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

			// Test if rserve is running
			try {
				new RConnection();
			} catch (Exception e) {
				try {
					Runtime.getRuntime().exec("R CMD Rserve");
				} catch (IOException e2) {
					System.err.println("Rserve could not be started");
					return null;
				}
			}

			try {
				instance = new RConnection();
				instance.eval("library(tuneR)");
				instance.eval("library(seewave)");
				instance.eval("library(quantmod)");
			} catch (RserveException e) {
				System.err.println("Rserve is not running");
			}
		}
		return instance;
	}
}
