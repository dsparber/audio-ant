package com.audioant.io.eventObserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.json.simple.JSONObject;

import com.audioant.audio.model.Sound;
import com.audioant.io.android.AndroidConnection;
import com.audioant.io.android.json.actions.RecognisedSoundAction;

/**
 *
 * @author Daniel Sparber
 * @year 2016
 *
 * @version 1.0
 */
public class AndroidEvents implements Observer {

	private static AndroidEvents androidEvents;

	public static AndroidEvents getInstance() throws IOException {
		if (androidEvents == null) {
			androidEvents = new AndroidEvents();
		}
		return androidEvents;
	}

	private List<Sound> lastNotified;

	private AndroidEvents() {
		lastNotified = new ArrayList<Sound>();
	}

	@Override
	public void update(Observable o, Object arg) {

		if (arg instanceof List<?> && ((List<?>) arg).get(0) instanceof Sound) {

			@SuppressWarnings("unchecked")
			List<Sound> sounds = (List<Sound>) arg;

			Events events = Events.getInstance();
			events.addAlertedSouds(lastNotified);

			List<Sound> newSounds = events.getNewSounds(sounds);

			if (!newSounds.isEmpty()) {
				RecognisedSoundAction action = new RecognisedSoundAction();
				action.setSoundList(newSounds);
				JSONObject request = action.createRequest();
				AndroidConnection.write(request.toJSONString());
			}

			lastNotified = newSounds;
		}
	}
}
