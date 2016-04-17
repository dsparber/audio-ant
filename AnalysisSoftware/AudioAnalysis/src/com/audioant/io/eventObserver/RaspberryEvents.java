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
import com.audioant.io.android.AndroidConnection;
import com.audioant.io.android.json.actions.AlertConfirmedAction;
import com.audioant.io.raspberry.AlertController;
import com.audioant.io.raspberry.ButtonController;
import com.audioant.io.raspberry.hardware.Button;

/**
 *
 * @author Daniel Sparber
 * @year 2016
 *
 * @version 1.0
 */
public class RaspberryEvents implements Observer {

	private static RaspberryEvents raspberryEvents;

	public static RaspberryEvents getInstance() throws IOException {
		if (raspberryEvents == null) {
			raspberryEvents = new RaspberryEvents();
		}
		return raspberryEvents;
	}

	private List<Sound> lastNotified;

	private RaspberryEvents() throws IOException {
		lastNotified = new ArrayList<Sound>();
		ButtonController.getInstance().addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {

		Events events = Events.getInstance();

		if (arg instanceof Button) {
			Button button = (Button) arg;
			if (button.equals(Button.BUTTON_CONFIRM)) {
				Events.reset();

				AlertConfirmedAction action = new AlertConfirmedAction();
				AndroidConnection.write(action.createRequest().toJSONString());
				try {
					AlertController.getInstance().confirmAlert();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (arg instanceof List<?> && ((List<?>) arg).get(0) instanceof Sound) {

			@SuppressWarnings("unchecked")
			List<Sound> sounds = (List<Sound>) arg;

			events.addAlertedSouds(lastNotified);
			List<Sound> newSounds = events.getNewSounds(sounds);

			try {
				AlertSettings settings = AlertSettings.getInstance();

				if (!newSounds.isEmpty()) {
					for (Sound sound : newSounds) {
						AlertController.getInstance().writeText(sound.getTextForDisplay());
					}
					System.out.println();
					if (settings.isLightSignals()) {
						AlertController.getInstance().blink();
					}
					if (settings.isAudioSignals()) {

						Integer id = LearnedSounds.getSound(newSounds.get(0).getId()).getAlertId();
						Integer defaultId = AlertSettings.getInstance().getAlertSoundId();

						String path = Config.FOLDER_GLOBAL + Config.ALERT_SOUNDS_FOLDER_PATH
								+ AlertSounds.getSoundFallback(id, defaultId).getId() + '/' + Config.ALERT_SOUNDS_FILE;

						AlertController.getInstance().playSound(path);
					}
				}

			} catch (IOException | ParseException e) {
				e.printStackTrace();
			}
			lastNotified = newSounds;
		}
	}
}
