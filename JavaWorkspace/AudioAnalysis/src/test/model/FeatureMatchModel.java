package test.model;

/**
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class FeatureMatchModel {

	private int id;
	private int resultId;

	private double strongestFrequencyMatch;

	public FeatureMatchModel(int resultId, double strongestFrequencyMatch) {
		super();
		this.resultId = resultId;
		this.strongestFrequencyMatch = strongestFrequencyMatch;
	}

	public int getResultId() {
		return resultId;
	}

	public double getStrongestFrequencyMatch() {
		return strongestFrequencyMatch;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}