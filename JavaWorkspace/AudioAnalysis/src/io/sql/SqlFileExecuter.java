package io.sql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlFileExecuter {

	public static void execute(Connection c, String filePath) throws IOException, SQLException, ClassNotFoundException {

		File file = new File(filePath);
		BufferedReader in = new BufferedReader(new FileReader(file));

		StringBuilder stringBuilder = new StringBuilder();

		String line;
		while ((line = in.readLine()) != null) {
			stringBuilder.append(line);
		}

		in.close();

		Statement statement = c.createStatement();
		statement.execute(stringBuilder.toString());

		statement.close();
		c.close();
	}

}
