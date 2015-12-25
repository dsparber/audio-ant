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
		super.soundfile = WorkingDir.AUDIO_FILE;
		recordAudio = new AudioRecorder(super.soundfile);
	}

	public void startCapturing() throws LineUnavailableException {
		recordAudio.startCapturing();
	}

	public void stopCapturing() {
		recordAudio.stopCapture();
	}
}
