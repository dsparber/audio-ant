package com.audioant.audio.learning;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

import com.audioant.audio.model.Sound;
import com.audioant.config.Parameters.SoundsXml;
import com.audioant.io.xml.LearnedSoundsXml;

public class LearnedSounds {

	private static LearnedSounds learnedSounds;

	private List<Sound> sounds;
	private LearnedSoundsXml soundsXml;

	public LearnedSounds() {

		try {
			soundsXml = new LearnedSoundsXml(SoundsXml.XML_FILE);
			sounds = soundsXml.read();
		} catch (FileNotFoundException | XMLStreamException e) {
			soundsXml = new LearnedSoundsXml(SoundsXml.XML_FILE);
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

	public static LearnedSoundsXml getSoundsXml() {

		if (learnedSounds == null) {
			learnedSounds = new LearnedSounds();
		}
		return learnedSounds.soundsXml;
	}

	public static int getNextNumber() {

		int max = 0;

		for (Sound soundModel : getSounds()) {
			if (soundModel.getNumber() > max) {
				max = soundModel.getNumber();
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
}