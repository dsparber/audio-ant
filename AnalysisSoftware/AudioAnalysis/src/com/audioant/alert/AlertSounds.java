package com.audioant.alert;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

import com.audioant.audio.model.Sound;
import com.audioant.config.Config;
import com.audioant.io.xml.SoundsXml;

/**
 *
 * @author Daniel Sparber
 * @year 2016
 *
 * @version 1.0
 */
public class AlertSounds {

	private static AlertSounds alertSounds;

	private List<Sound> sounds;
	private SoundsXml soundsXml;

	public AlertSounds() {

		try {
			soundsXml = new SoundsXml(Config.LEARNED_SOUNDS_XML_FILE_PATH);
			sounds = soundsXml.read();
		} catch (FileNotFoundException | XMLStreamException e) {
			sounds = new ArrayList<Sound>();
		}
	}

	private static AlertSounds getInstance() {

		if (alertSounds == null) {
			alertSounds = new AlertSounds();
		}
		return alertSounds;
	}

	private int getNextNumber() {
		int max = 0;
		for (Sound sound : sounds) {
			if (sound.getId() > max) {
				max = sound.getId();
			}
		}
		return max + 1;
	}

	private void saveSounds() throws ParserConfigurationException, TransformerException {
		soundsXml.write(sounds);
	}

	// Operations
	public static void deleteSound(int id) throws ParserConfigurationException, TransformerException {
		for (int i = 0; i < getInstance().sounds.size(); i++) {
			if (getInstance().sounds.get(i).getId() == id) {
				getInstance().sounds.remove(i);
				break;
			}

		}
		getInstance().saveSounds();
	}

	public static void addSound(Sound sound) throws ParserConfigurationException, TransformerException {
		alertSounds.sounds.add(sound);
		getInstance().saveSounds();
	}

	public static List<Sound> getAllSounds() {
		return getInstance().sounds;
	}

	public static Sound getNewSound() {
		return new Sound(null, Config.ALERT_SOUNDS_FOLDER, getInstance().getNextNumber());
	}

	public static Sound getNewSound(String name) {
		return new Sound(name, Config.ALERT_SOUNDS_FOLDER, getInstance().getNextNumber());
	}

	public static Sound getSound(Integer id) {

		if (id == null) {
			return null;
		}

		for (Sound s : getInstance().sounds) {
			if (s.getId() == id) {
				return s;
			}
		}
		return null;
	}

	public static Sound getSoundFallback(Integer id, int defaultSoundId) {
		Sound s = getSound(id);
		if (s == null) {
			s = getSound(defaultSoundId);
		}
		if (s == null) {
			if (getAllSounds().size() > 0) {
				s = getSound(0);
			}
		}
		return s;
	}

}
