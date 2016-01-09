package com.audioant.audio.learning;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.audioant.audio.model.SoundModel;
import com.audioant.config.Parameters.WorkingDir;

public class LearnedSounds {

	private static LearnedSounds learnedSounds;

	private List<SoundModel> sounds;

	public LearnedSounds() {

		sounds = new ArrayList<SoundModel>();

		File[] learnedSounds = new File(WorkingDir.FOLDER_LEARNED_SOUNDS).listFiles();

		for (File file : learnedSounds) {
			if (file.isDirectory()) {

				String soundName = file.getName();

				SoundModel soundModel;

				if (Character.isAlphabetic(soundName.charAt(0))) {
					soundModel = new SoundModel(soundName);
				} else {
					int soundNumber = Integer.parseInt(soundName);
					soundModel = new SoundModel(soundNumber);
				}

				sounds.add(soundModel);
			}
		}
	}

	public static void addSound(SoundModel soundModel) {
		learnedSounds.sounds.add(soundModel);
	}

	public static List<SoundModel> getSounds() {

		if (learnedSounds == null) {
			learnedSounds = new LearnedSounds();
		}
		return learnedSounds.sounds;
	}

	public static SoundModel getNewUnnamedSoundModel() {

		int max = 0;

		for (SoundModel soundModel : getSounds()) {
			if (soundModel.isUnnamed()) {
				if (soundModel.getSoundNumber() > max) {
					max = soundModel.getSoundNumber();
				}
			}
		}

		return new SoundModel(max + 1);
	}

}