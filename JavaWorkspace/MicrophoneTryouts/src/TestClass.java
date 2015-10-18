import javax.sound.sampled.LineUnavailableException;

public class TestClass {

	public static void main(String[] args) throws LineUnavailableException, InterruptedException {
		RecordAudio recordAudio = new RecordAudio();

		System.out.println("Start");
		recordAudio.captureAudio();

		Thread.sleep(3000);

		recordAudio.stopCapture();
		System.out.println("Stop");

	}
}
