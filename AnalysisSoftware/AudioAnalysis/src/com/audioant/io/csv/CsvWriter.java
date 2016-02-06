package com.audioant.io.csv;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.audioant.config.Config;

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

				writer.append(Config.CSV_SEPERATOR_TEXT);
				writer.write(objects[i].toString());
				writer.append(Config.CSV_SEPERATOR_TEXT);

				if (i < objects.length - 1) {
					writer.append(Config.CSV_SEPERATOR_VALUE);
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
