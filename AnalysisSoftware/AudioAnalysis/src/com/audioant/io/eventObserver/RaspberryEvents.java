package com.audioant.io.eventObserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.json.simple.parser.ParseException;

import com.audioant.alert.AlertSettings;
import com.audioant.alert.AlertSounds;
import com.audioant.audio.learning.LearnedSounds;
import com.audioant.audio.model.Sound;
import com.audioant.config.Config;
import com.audioant.io.raspberry.AlertController;
import com.audioant.io.raspberry.ButtonController;
import com.audioant.io.raspberry.DisplayController;
import com.audioant.io.raspberry.SoundController;
import com.audioant.io.raspberry.hardware.Button;

/**
 *
 * @author Daniel Sparber
 * @year 2016
 *
 * @version 1.0
 */
public class RaspberryEvents implements Observer {

	private List<Sound> lastSounds;
	private List<Sound> alertedSounds;

	public RaspberryEvents() throws IOException {
		lastSounds = new ArrayList<Sound>();
		alertedSounds = new ArrayList<Sound>();
		ButtonController.getInstance().addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {

		if (arg instanceof Button) {
			Button button = (Button) arg;
			if (button.equals(Button.BUTTON_CONFIRM)) {
				lastSounds = new ArrayList<Sound>();
				alertedSounds = new ArrayList<Sound>();
			}
		}

		if (arg instanceof List<?> && ((List<?>) arg).get(0) instanceof Sound) {

			@SuppressWarnings("unchecked")
			List<Sound> sounds = (List<Sound>) arg;

			List<Sound> newSounds = getNewSounds(sounds);

			try {
				AlertSettings settings = AlertSettings.getInstance();

				if (notNotified(newSounds)) {
					if (settings.isLightSignals()) {
						AlertController.getInstance().blink();

						if (settings.isAudioSignals()) {

							Integer id = LearnedSounds.getSound(newSounds.get(0).getId()).getAlertId();
							Integer defaultId = AlertSettings.getInstance().getAlertSoundId();

							String path = Config.FOLDER_GLOBAL + Config.ALERT_SOUNDS_FOLDER_PATH
									+ AlertSounds.getSoundFallback(id, defaultId).getId() + '/'
									+ Config.ALERT_SOUNDS_FILE;

							SoundController.getInstance().play(path);
						}
					}
				}
				if (!newSounds.isEmpty()) {
					for (Sound sound : newSounds) {
						DisplayController.getInstance().write(sound.getTextForDisplay());
					}
				}

			} catch (IOException | ParseException e) {
				e.printStackTrace();
			}

			alertedSounds.addAll(newSounds);
		}
	}

	private boolean notNotified(List<Sound> newSounds) {
		return newSounds.size() == lastSounds.size();
	}

	private List<Sound> getNewSounds(List<Sound> sounds) {

		for (Sound sound : sounds) {
			lastSounds.add(sound);
		}

		List<Sound> newSounds = new ArrayList<Sound>();

		for (Sound sound : lastSounds) {
			if (!alertedSounds.contains(sound)) {
				newSounds.add(sound);
			}
		}
		return newSounds;
	}
}
