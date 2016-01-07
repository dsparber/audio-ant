package com.audioant.audio.model;

import com.audioant.config.Parameters.WorkingDir;

public class SoundModel {

	private String soundName;
	private String folder;

	public SoundModel(String soundName) {
		this.soundName = soundName;

		folder = WorkingDir.FOLDER_LEARNED_SOUNDS + soundName + '/';
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
}
