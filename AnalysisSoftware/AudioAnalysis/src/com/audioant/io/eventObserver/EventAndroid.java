package com.audioant.io.eventObserver;

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
public class EventAndroid implements Observer {

	@Override
	public void update(Observable o, Object arg) {

		if (arg instanceof List<?> && ((List<?>) arg).get(0) instanceof Sound) {

			@SuppressWarnings("unchecked")
			List<Sound> sounds = (List<Sound>) arg;

			RecognisedSoundAction action = new RecognisedSoundAction();
			action.setSoundList(sounds);
			JSONObject request = action.createRequest();
			AndroidConnection.write(request.toJSONString());
		}
	}
}
