package com.audioant.config;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Daniel Sparber
 * @year 2016
 *
 * @version 1.0
 */
public class ConfigReader {

	private static final String CONFIG_FILE = "com/audioant/config.properties";

	private Properties properties;

	private static ConfigReader configReader;

	public ConfigReader() throws IOException {
		properties = new Properties();

		BufferedInputStream stream = new BufferedInputStream(new FileInputStream(CONFIG_FILE));
		properties.load(stream);

		stream.close();
	}

	private static ConfigReader getConfigReader() {

		if (configReader == null) {
			try {
				configReader = new ConfigReader();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return configReader;
	}

	public static String getProperty(String key) {
		return getConfigReader().properties.getProperty(key);
	}

	public static int getPropertyAsInt(String key) {
		return Integer.parseInt(getProperty(key));
	}

	public static double getPropertyAsDouble(String key) {
		return Double.parseDouble(getProperty(key));
	}

	public static float getPropertyAsFloat(String key) {
		return Float.parseFloat(getProperty(key));
	}

	public static boolean getPropertyAsBoolean(String key) {
		return getProperty(key) == "true";
	}

	public static char getPropertyAsChar(String key) {
		return getProperty(key).charAt(0);
	}

}
