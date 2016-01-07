package com.audioant;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RserveException;

import com.audioant.audio.analysis.AudioStreamAnalyser;
import com.audioant.audio.learning.MicrophoneSoundLearner;
import com.audioant.io.eventObserver.EventLights;
import com.audioant.io.eventObserver.EventLogger;
import com.audioant.io.raspberry.ButtonController;
import com.audioant.io.raspberry.LedController;
import com.audioant.io.raspberry.hardware.LEDS;

public class RaspberryTool implements Observer {

	private static AudioStreamAnalyser analyser;
	private static MicrophoneSoundLearner learner;

	private LedController ledController;
	private ButtonController buttonController;

	private boolean recording = false;

	public RaspberryTool() throws IOException {

		ledController = new LedController();
		buttonController = new ButtonController();
		buttonController.addObserver(this);
	}

	public static void main(String[] args) {

		try {
			RaspberryTool raspberryTool = new RaspberryTool();
			raspberryTool.startAnalysis();

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
			analyser.start();

		} catch (RserveException | IOException e) {
			e.printStackTrace();
		}
	}

	private void stopAnalysis() {
		analyser.stop();
	}

	@Override
	public void update(Observable o, Object arg) {

		if (recording) {

			recording = true;
			try {
				ledController.on(LEDS.LED_RECORDING);
			} catch (IOException e) {
				e.printStackTrace();
			}
			stopAnalysis();

			// TODO: unnamed sounds
			learner = new MicrophoneSoundLearner("Name");

			try {
				learner.startCapturing();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}

		} else {

			learner.stopCapturing();
			try {
				learner.extractFeatures();
			} catch (LineUnavailableException | IOException | REngineException | REXPMismatchException
					| UnsupportedAudioFileException e) {
				e.printStackTrace();
			}

			recording = false;
			try {
				ledController.off(LEDS.LED_RECORDING);
			} catch (IOException e) {
				e.printStackTrace();
			}
			startAnalysis();
		}
	}
}