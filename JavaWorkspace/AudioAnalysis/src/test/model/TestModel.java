package test.model;

import java.sql.Timestamp;;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class TestModel {

	private Timestamp date;
	private String fileType;
	private int id;

	public TestModel(String fileType) {
		date = new Timestamp(System.currentTimeMillis());
		this.fileType = fileType;
	}

	public Timestamp getTimestamp() {
		return date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFileType() {
		return fileType;
	}
}