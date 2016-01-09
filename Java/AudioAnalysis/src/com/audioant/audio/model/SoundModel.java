package com.audioant.audio.model;

import com.audioant.audio.learning.LearnedSounds;
import com.audioant.config.Parameters.WorkingDir;

public class SoundModel {

	private int soundNumber;
	private String soundName;
	private String folder;

	public SoundModel(String soundName) {

		if (soundName == null) {
			createUnnamedModel();
		} else {
			this.soundName = soundName;
			folder = WorkingDir.FOLDER_LEARNED_SOUNDS + soundName + '/';
		}
	}

	public SoundModel(int soundNumber) {

		this.soundNumber = soundNumber;
		folder = WorkingDir.FOLDER_LEARNED_SOUNDS + soundNumber + '/';
	}

	public SoundModel() {
		createUnnamedModel();
	}

	private void createUnnamedModel() {
		SoundModel model = LearnedSounds.getNewUnnamedSoundModel();
		soundNumber = model.getSoundNumber();
		folder = model.getFolder();
	}

	public String getSoundName() {
		return soundName;
	}

	public void setSoundName(String soundName) {
		this.soundName = soundName;
	}

	public String getFolder() {
		return folder;
	}

	public int getSoundNumber() {
		return soundNumber;
	}

	public boolean isUnnamed() {
		return soundName == null;
	}
}
