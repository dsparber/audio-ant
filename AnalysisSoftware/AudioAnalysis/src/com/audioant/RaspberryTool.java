package com.audioant;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RserveException;

import com.audioant.audio.analysis.AudioStreamAnalyser;
import com.audioant.audio.learning.LearnedSounds;
import com.audioant.audio.learning.MicrophoneSoundLearner;
import com.audioant.audio.model.Sound;
import com.audioant.config.Config;
import com.audioant.io.android.AndroidConnection;
import com.audioant.io.eventObserver.AndroidEvents;
import com.audioant.io.eventObserver.DetailLogger;
import com.audioant.io.eventObserver.EventLogger;
import com.audioant.io.eventObserver.RaspberryEvents;
import com.audioant.io.raspberry.ButtonController;
import com.audioant.io.raspberry.LedController;
import com.audioant.io.raspberry.hardware.Button;
import com.audioant.io.raspberry.hardware.Led;

public class RaspberryTool implements Observer {

	private static AudioStreamAnalyser analyser;
	private static MicrophoneSoundLearner learner;
	private static LedController ledController;

	private ButtonController buttonController;

	private boolean recording = false;

	private Sound newSound;

	private RaspberryTool() throws IOException {

		AndroidConnection.open();

		buttonController = ButtonController.getInstance();
		buttonController.addObserver(this);
	}

	public static void start() {

		try {
			ledController = LedController.getInstance();
			new RaspberryTool();
			if (!LearnedSounds.getSounds().isEmpty()) {
				RaspberryTool.startAnalysis();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void startAnalysis() {
		try {
			if (analyser == null) {

				analyser = new AudioStreamAnalyser();
				analyser.addObserver(new EventLogger());
				analyser.addObserver(new DetailLogger());
				analyser.addObserver(new RaspberryEvents());
				analyser.addObserver(new AndroidEvents());
			}
			if (!analyser.isRunning()) {
				analyser.start();
				analyser.reloadLearnedSounds();
				led(Led.LED_RUNNING, 1);
			}

		} catch (RserveException | IOException e) {
			e.printStackTrace();
			led(Led.LED_RUNNING, 0);
		}
	}

	private static void stopAnalysis() {
		if (analyser != null && analyser.isRunning()) {
			analyser.stop();
			led(Led.LED_RUNNING, 0);
		}
	}

	@Override
	public void update(Observable o, Object arg) {

		Button button = (Button) arg;

		if (button == Button.BUTTON_RECORDING) {
			if (!recording) {

				stopAnalysis();

				newSound = new Sound(LearnedSounds.getNextNumber(), Config.LEARNED_SOUNDS_FILE);

				recording = true;
				led(Led.LED_RECORDING, 1);

				learner = new MicrophoneSoundLearner(newSound);

				try {
					learner.startCapturing();
				} catch (LineUnavailableException e) {
					e.printStackTrace();

					recording = false;
					led(Led.LED_RECORDING, 0);

					startAnalysis();
				}

			} else {

				try {
					learner.stopCapturing();

					recording = false;
					led(Led.LED_RECORDING, 0);

					learner.extractFeatures();

					LearnedSounds.addSound(newSound);
					LearnedSounds.saveSounds();

					startAnalysis();
					led(Led.LED_RECORDING_SUCCESS, 5);

				} catch (LineUnavailableException | IOException | REngineException | REXPMismatchException
						| UnsupportedAudioFileException | ParserConfigurationException | TransformerException
						| IndexOutOfBoundsException e) {

					startAnalysis();
					LearnedSounds.deleteSound(newSound.getId());
					led(Led.LED_RECORDING_FAILED, 5);
					e.printStackTrace();
				}
			}
		}
	}

	private static void led(Led led, int times) {
		try {
			if (times > 1) {
				ledController.blink(led, times);
			} else if (times == 1) {
				ledController.on(led);
			} else {
				ledController.off(led);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void restartAnalysis() {
		stopAnalysis();
		startAnalysis();
	}
}