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
import com.audioant.io.eventObserver.EventLights;
import com.audioant.io.eventObserver.EventLogger;
import com.audioant.io.raspberry.ButtonController;
import com.audioant.io.raspberry.LedController;
import com.audioant.io.raspberry.hardware.Button;
import com.audioant.io.raspberry.hardware.Led;

public class RaspberryTool implements Observer {

	private static AudioStreamAnalyser analyser;
	private static MicrophoneSoundLearner learner;

	private LedController ledController;
	private ButtonController buttonController;

	private boolean recording = false;

	private Sound newSound;

	public RaspberryTool() throws IOException {

		ledController = new LedController();
		buttonController = new ButtonController();
		buttonController.addObserver(this);
	}

	public static void main(String[] args) {

		try {
			RaspberryTool raspberryTool = new RaspberryTool();
			if (!LearnedSounds.getSounds().isEmpty()) {
				raspberryTool.startAnalysis();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void startAnalysis() {
		try {
			if (analyser == null) {

				analyser = new AudioStreamAnalyser();
				analyser.addObserver(new EventLogger());
				analyser.addObserver(new EventLights());
			}
			if (!analyser.isRunning()) {
				analyser.start();
				analyser.reloadLearnedSounds();
			}

		} catch (RserveException | IOException e) {
			e.printStackTrace();
		}
	}

	private void stopAnalysis() {
		if (analyser != null && analyser.isRunning()) {
			analyser.stop();
		}
	}

	@Override
	public void update(Observable o, Object arg) {

		Button button = (Button) arg;

		if (button == Button.BUTTON_RECORDING) {
			if (!recording) {

				stopAnalysis();

				newSound = LearnedSounds.getNewUnnamedSound();

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

				} catch (LineUnavailableException | IOException | REngineException | REXPMismatchException
						| UnsupportedAudioFileException | ParserConfigurationException | TransformerException
						| IndexOutOfBoundsException e) {

					led(Led.LED_WARNING, 5);
				}
			}
		}
	}

	private void led(Led led, int times) {
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
}