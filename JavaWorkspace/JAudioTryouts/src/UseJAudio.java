import java.io.FileOutputStream;
import java.util.HashMap;

import jAudioFeatureExtractor.Controller;
import jAudioFeatureExtractor.ACE.DataTypes.Batch;
import jAudioFeatureExtractor.DataTypes.RecordingInfo;

/**
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */

public class UseJAudio {
	public static void main(String[] args) throws Exception {
		Batch b = new Batch();
		HashMap<String, String[]> attributes = new HashMap<String, String[]>();
		HashMap<String, Boolean> activated = new HashMap<String, Boolean>();

		String[] attrs = { "100" };

		attributes.put("Running Mean of Zero Crossings", attrs);
		attributes.put("Standard Deviation of Zero Crossings", attrs);
		activated.put("Running Mean of Zero Crossings", true);
		activated.put("Standard Deviation of Zero Crossings", true);

		b.setFeatures(activated, attributes);

		RecordingInfo[] recording = { new RecordingInfo("res/audio/Klingel.wav"),
				new RecordingInfo("res/audio/Mikrowelle.wav") };

		int windowSize = 512;
		double windowOverlap = 0;
		double samplingRate = 16000;
		boolean normalise = true;
		boolean perWindow = false;
		boolean overall = true;
		int outputType = 0;

		b.setDataModel(new Controller().dm_);
		b.getDataModel().featureKey = new FileOutputStream("featureKey.xml");
		b.getDataModel().featureValue = new FileOutputStream("featureValue.xml");
		b.setRecording(recording);
		b.setSettings(windowSize, windowOverlap, samplingRate, normalise, perWindow, overall, outputType);

		String[] aggNames = { "Standard Deviation" };
		String[][] aggFeatures = { { "Standard Deviation of Zero Crossings" } };
		String[][] aggParam = { { "100" } };
		b.setAggregators(aggNames, aggFeatures, aggParam);

		b.execute();

		double[][] out = b.getDataModel().container.getResults();

		for (double[] ds : out) {
			for (double d : ds) {
				System.out.print(d + "\t");
			}
			System.out.println();
		}

	}
}
