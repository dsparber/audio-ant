package io.csv;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import config.Parameters.Csv;

/**
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class CsvWriter {

	private String filePath;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public CsvWriter(String filePath) {
		this.filePath = filePath;
	}

	public void writeMatrix(Object[][] matrix) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false));

		for (Object[] objects : matrix) {
			for (int i = 0; i < objects.length; i++) {

				writer.append(Csv.TEXT_SEPERATOR);
				writer.write(objects[i].toString());
				writer.append(Csv.TEXT_SEPERATOR);

				if (i < objects.length - 1) {
					writer.append(Csv.VALUE_SEPERATOR);
				}
			}
			writer.append('\n');
		}

		writer.flush();
		writer.close();
	}

	public void writeSingleColmn(Object[] col) throws IOException {

		Object[][] matrix = new Object[col.length][1];

		for (int i = 0; i < col.length; i++) {
			matrix[i][0] = col[i];
		}

		writeMatrix(matrix);
	}

}
