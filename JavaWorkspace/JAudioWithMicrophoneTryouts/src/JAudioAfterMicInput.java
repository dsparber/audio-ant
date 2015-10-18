/**
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class JAudioAfterMicInput {

	// Produces mysterious bug
	// ----------------------------------------------------------------------
	// 2015-10-18 21:05:25.212 java[13497:3722727] !!! BUG: The current event
	// queue and the main event queue are not the same.
	// Events will not be handled correctly. This is probably because
	// _TSGetMainThread was called for the first time off the main thread.
	// ----------------------------------------------------------------------

	public static void main(String[] args) throws Exception {

		final String OUT = "out/JAudioAfterMicInput.wav";

		RecordAudio recordAudio = new RecordAudio(OUT);

		recordAudio.startCapturing();
		Thread.sleep(3000);
		recordAudio.stopCapture();

		UseJAudio useJAudio = UseJAudio.builder().build();
		useJAudio.createNewBatch(OUT);
		useJAudio.setTestValues();
		double[][] results = useJAudio.getResults("out/featureKeys.xml", "out/featureValues.xml");

		for (double[] ds : results) {
			for (double d : ds) {
				System.out.print(d + "\t");
			}
			System.out.println();
		}
	}
}
