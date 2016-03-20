package com.audioant.audio.learning;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

import com.audioant.audio.model.Sound;
import com.audioant.config.Config;
import com.audioant.io.xml.SoundsXml;

public class LearnedSounds {

	private static LearnedSounds learnedSounds;

	private List<Sound> sounds;
	private SoundsXml soundsXml;

	public LearnedSounds() {

		try {
			soundsXml = new SoundsXml(Config.LEARNED_SOUNDS_XML_FILE_PATH);
			sounds = soundsXml.read();
		} catch (FileNotFoundException | XMLStreamException e) {
			soundsXml = new SoundsXml(Config.LEARNED_SOUNDS_XML_FILE_PATH);
			sounds = new ArrayList<Sound>();
		}
	}

	public static void addSound(Sound soundModel) {
		getSounds().add(soundModel);
	}

	public static List<Sound> getSounds() {

		if (learnedSounds == null) {
			learnedSounds = new LearnedSounds();
		}
		return learnedSounds.sounds;
	}

	public static SoundsXml getSoundsXml() {

		if (learnedSounds == null) {
			learnedSounds = new LearnedSounds();
		}
		return learnedSounds.soundsXml;
	}

	public static int getNextNumber() {

		int max = 0;

		for (Sound soundModel : getSounds()) {
			if (soundModel.getId() > max) {
				max = soundModel.getId();
			}
		}

		return max + 1;
	}

	public static Sound getNewUnnamedSound() {
		return new Sound(null, getNextNumber());
	}

	public static Sound getNewSound(String name) {
		return new Sound(name, getNextNumber());
	}

	public static void saveSounds() throws ParserConfigurationException, TransformerException {
		getSoundsXml().write(getSounds());
	}

	public static void deleteSound(int id) {
		for (int i = 0; i < getSounds().size(); i++) {
			if (getSounds().get(i).getId() == id) {
				getSounds().remove(i);
				break;
			}

		}

	}
}