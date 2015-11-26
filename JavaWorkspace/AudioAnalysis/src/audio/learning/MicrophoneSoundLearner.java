package audio.learning;

import javax.sound.sampled.LineUnavailableException;

import config.Parameters.WorkingDir;
import io.microphone.AudioRecorder;

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
		super.pathnameIn = WorkingDir.FOLDER_LEARNED_SOUNDS + WorkingDir.AUDIO_FILE;
		recordAudio = new AudioRecorder(super.pathnameIn);
	}

	public void startCapturing() throws LineUnavailableException {
		recordAudio.startCapturing();
	}

	public void stopCapturing() {
		recordAudio.stopCapture();
	}
}
