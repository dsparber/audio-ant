package io.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import singleton.AudioTestDatabaseConnection;
import test.model.FeatureMatchModel;
import test.model.ResultModel;
import test.model.SoundModel;
import test.model.TestModel;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class AutomatedTestDatabaseManager {

	private Connection connection;

	public AutomatedTestDatabaseManager() throws SQLException {

		connection = AudioTestDatabaseConnection.getUniqueInstance();
	}

	public int insert(TestModel model) throws SQLException {

		String sql = "INSERT INTO Tests (TestDate) VALUES (?)";

		PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		statement.setTimestamp(1, model.getTimestamp());

		statement.executeUpdate();

		ResultSet rs = statement.getGeneratedKeys();
		rs.first();
		int id = rs.getInt(1);

		statement.close();
		rs.close();

		model.setId(id);
		return id;
	}

	public int insert(SoundModel model) throws SQLException {

		String sql = "SELECT * FROM Sounds WHERE name = ?";

		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, model.getName());
		ResultSet rs = statement.executeQuery();

		int id;
		if (rs.first()) {

			id = rs.getInt("id");

		} else {

			sql = "INSERT INTO Sounds (name) VALUES (?)";

			statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, model.getName());

			statement.executeUpdate();

			rs = statement.getGeneratedKeys();
			rs.first();
			id = rs.getInt(1);
		}

		statement.close();
		rs.close();

		model.setId(id);
		return id;
	}

	public int insert(ResultModel model) throws SQLException {

		String sql = "INSERT INTO Results (testId, soundId, fileName, shouldBeRecognised, wasRecognised, correctRecognition) VALUES (?, ?, ?, ?, ?, ?)";

		PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		statement.setInt(1, model.getTestId());
		statement.setInt(2, model.getSoundId());
		statement.setString(3, model.getFileName());
		statement.setBoolean(4, model.isShouldBeRecognised());
		statement.setBoolean(5, model.isWasRecognised());
		statement.setBoolean(6, model.isCorrectRecognition());

		statement.executeUpdate();

		ResultSet rs = statement.getGeneratedKeys();
		rs.first();
		int id = rs.getInt(1);

		statement.close();
		rs.close();

		model.setId(id);
		return id;
	}

	public int insert(FeatureMatchModel model) throws SQLException {

		String sql = "INSERT INTO FeatureMatch (resultId, strongestFrequency) VALUES (?, ?)";

		PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		statement.setInt(1, model.getResultId());
		statement.setDouble(2, model.getStrongestFrequencyMatch());

		statement.executeUpdate();

		ResultSet rs = statement.getGeneratedKeys();
		rs.first();
		int id = rs.getInt(1);

		statement.close();
		rs.close();

		model.setId(id);
		return id;
	}
}
