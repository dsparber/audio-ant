import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.LinkedList;

import jAudioFeatureExtractor.Controller;
import jAudioFeatureExtractor.ACE.DataTypes.Batch;
import jAudioFeatureExtractor.DataTypes.RecordingInfo;

/**
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.1
 */

public class UseJAudio {

	private Batch batch;

	private int windowSize, outputType;
	private double windowOverlap, samplingRate;
	private boolean normalise, perWindow, overall;

	public static class Builder {

		private int windowSize = 512, outputType = 0;
		private double windowOverlap = 0, samplingRate = 16000;
		private boolean normalise = true, perWindow = false, overall = true;

		public Builder windowSize(int windowSize) {
			this.windowSize = windowSize;
			return this;
		}

		public Builder outputType(int outputType) {
			this.outputType = outputType;
			return this;
		}

		public Builder windowOverlap(double windowOverlap) {
			this.windowOverlap = windowOverlap;
			return this;
		}

		public Builder normalise(boolean normalise) {
			this.normalise = normalise;
			return this;
		}

		public Builder perWindow(boolean perWindow) {
			this.perWindow = perWindow;
			return this;
		}

		public Builder overall(boolean overall) {
			this.overall = overall;
			return this;
		}

		public UseJAudio build() {
			return new UseJAudio(windowSize, windowOverlap, samplingRate, normalise, perWindow, overall, outputType);
		}

	}

	public static Builder builder() {
		return new Builder();
	}

	private UseJAudio(int windowSize, double windowOverlap, double samplingRate, boolean normalise, boolean perWindow,
			boolean overall, int outputType) {
		this.windowSize = windowSize;
		this.windowOverlap = windowOverlap;
		this.samplingRate = samplingRate;
		this.normalise = normalise;
		this.perWindow = perWindow;
		this.overall = overall;
		this.outputType = outputType;
	}

	public void createNewBatch(String... filePaths) {

		batch = new Batch();

		LinkedList<RecordingInfo> recordings = new LinkedList<RecordingInfo>();
		for (String path : filePaths) {
			recordings.add(new RecordingInfo(path));
		}
		batch.setRecording((RecordingInfo[]) recordings.toArray());

		batch.setSettings(windowSize, windowOverlap, samplingRate, normalise, perWindow, overall, outputType);

	}

	public void setFeatures(HashMap<String, Boolean> activatedFeatures, HashMap<String, String[]> featureAttributes) {
		batch.setFeatures(activatedFeatures, featureAttributes);
	}

	public void setAggregators(String[] aggNames, String[][] aggFeatures, String[][] aggParam) {
		batch.setAggregators(aggNames, aggFeatures, aggParam);
	}

	public void setTestValues() {

		HashMap<String, String[]> attributes = new HashMap<String, String[]>();
		HashMap<String, Boolean> activated = new HashMap<String, Boolean>();

		String[] attrs = { "100" };

		attributes.put("Running Mean of Zero Crossings", attrs);
		attributes.put("Standard Deviation of Zero Crossings", attrs);
		activated.put("Running Mean of Zero Crossings", true);
		activated.put("Standard Deviation of Zero Crossings", true);

		setFeatures(activated, attributes);

		String[] aggNames = { "Standard Deviation" };
		String[][] aggFeatures = { { "Standard Deviation of Zero Crossings" } };
		String[][] aggParam = { { "100" } };

		setAggregators(aggNames, aggFeatures, aggParam);
	}

	public double[][] getResults(String pathFeatureKeyOutput, String pathFeatureValueOutput) throws Exception {

		batch.setDataModel(new Controller().dm_);
		batch.getDataModel().featureKey = new FileOutputStream(pathFeatureKeyOutput);
		batch.getDataModel().featureValue = new FileOutputStream(pathFeatureValueOutput);

		batch.execute();

		return batch.getDataModel().container.getResults();

	}
}
