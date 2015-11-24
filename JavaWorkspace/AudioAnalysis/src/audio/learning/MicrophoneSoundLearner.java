package audio.learning;

import javax.sound.sampled.LineUnavailableException;

import io.microphone.AudioRecorder;
import io.parameters.WorkingDirectory;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */

public class MicrophoneSoundLearner extends SoundLearner {

	private AudioRecorder recordAudio;

	public MicrophoneSoundLearner() {
		super.pathnameIn = WorkingDirectory.FOLDER + WorkingDirectory.AUDIO_FILE;
		recordAudio = new AudioRecorder(super.pathnameIn);
	}

	public void startCapturing() throws LineUnavailableException {
		recordAudio.startCapturing();
	}

	public void stopCapturing() {
		recordAudio.stopCapture();
	}
}
