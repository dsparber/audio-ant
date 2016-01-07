import javax.sound.sampled.LineUnavailableException;

public class TestClass {

	public static void main(String[] args) throws LineUnavailableException, InterruptedException {
		RecordAudio recordAudio = new RecordAudio("out/recording.wav");

		System.out.println("Start");
		recordAudio.startCapturing();

		Thread.sleep(3000);

		recordAudio.stopCapture();
		System.out.println("Stop");

	}
}
