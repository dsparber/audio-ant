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
	private int id;

	public TestModel() {
		date = new Timestamp(System.currentTimeMillis());

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
}
