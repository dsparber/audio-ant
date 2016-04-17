package com.audioant.io.eventObserver;

import java.util.ArrayList;
import java.util.List;

import com.audioant.audio.model.Sound;

/**
 *
 * @author Daniel Sparber
 * @year 2016
 *
 * @version 1.0
 */
public class Events {

	private static Events events;

	public static Events getInstance() {
		if (events == null) {
			events = new Events();
		}
		return events;
	}

	private List<Sound> lastSounds;
	private List<Sound> alertedSounds;

	private Events() {
		lastSounds = new ArrayList<Sound>();
		alertedSounds = new ArrayList<Sound>();
	}

	public void addAlertedSouds(List<Sound> sounds) {
		alertedSounds.addAll(sounds);
	}

	public static void reset() {
		events = null;
	}

	public boolean notNotified(List<Sound> newSounds) {
		return newSounds.size() == lastSounds.size();
	}

	public List<Sound> getNewSounds(List<Sound> sounds) {

		for (Sound sound : sounds) {
			lastSounds.add(sound);
		}

		List<Sound> newSounds = new ArrayList<Sound>();

		for (Sound sound : lastSounds) {
			if (!alertedSounds.contains(sound) && !newSounds.contains(sound)) {
				newSounds.add(sound);
			}
		}
		return newSounds;
	}
}
