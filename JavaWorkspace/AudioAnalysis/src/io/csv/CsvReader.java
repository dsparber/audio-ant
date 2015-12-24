package io.csv;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import config.Parameters.Csv;

/**
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class CsvReader {

	private String filePath;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public CsvReader(String filePath) {
		this.filePath = filePath;
	}

	public String[][] readMatrix() throws IOException {

		BufferedReader reader = new BufferedReader(new FileReader(filePath));

		ArrayList<String[]> lines = new ArrayList<String[]>();

		String line;
		while ((line = reader.readLine()) != null) {

			line = line.substring(1, line.length() - 1);
			String[] lineArray = line.split(Csv.SEPERATOR);
			lines.add(lineArray);
		}
		reader.close();

		int maxWidth = 0;
		for (String[] s : lines) {
			if (s.length > maxWidth) {
				maxWidth = s.length;
			}
		}

		String[][] matrix = new String[lines.size()][maxWidth];

		for (int i = 0; i < lines.size(); i++) {
			matrix[i] = lines.get(i);
		}

		return matrix;
	}

	public String[] readSingleCol(int index) throws IOException {

		String[][] matrix = readMatrix();

		String[] col = new String[matrix.length];

		for (int i = 0; i < col.length; i++) {
			col[i] = matrix[i][index];
		}

		return col;
	}

}
