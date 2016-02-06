package com.audioant.singleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.audioant.config.Config;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class AudioTestDatabaseConnection {

	/** unique instance */
	private static Connection instance = null;

	/**
	 * Get the unique instance of this class.
	 */
	public static synchronized Connection getUniqueInstance() {

		if (instance == null) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				instance = DriverManager.getConnection(
						"jdbc:mysql://" + Config.DATABASE_HOST + '/' + Config.DATABASE_TEST, Config.DATABASE_USER,
						Config.DATABASE_PASSWORD);
			} catch (ClassNotFoundException | SQLException e) {
				System.err.println("Database connection failed.");
			}
		}
		return instance;
	}
}
