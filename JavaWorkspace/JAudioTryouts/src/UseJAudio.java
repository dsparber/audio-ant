import java.util.HashMap;

import jAudioFeatureExtractor.ACE.DataTypes.Batch;

/**
 * @author Daniel Sparber
 *
 * @date 13. Okt. 2015
 * @version 1.0
 */

public class UseJAudio {
	public static void main(String[] args) {
		Batch b = new Batch();
		HashMap<String, String[]> attributes = new HashMap<String, String[]>();
		HashMap<String, Boolean> activated = new HashMap<String, Boolean>();

		b.setFeatures(activated, attributes);
	}
}
