package com.audioant.audio.model;

import com.audioant.config.Config;

public class Sound {

	private int id;
	private String name;
	private String path;
	private String soundFile;
	private Integer alertId;

	public Sound(String soundFile) {
		this.soundFile = soundFile;
	}

	public Sound(int number, String soundFile) {
		this(null, number, soundFile);
	}

	public Sound(String name, int number, String soundFile) {
		this(name, Config.LEARNED_SOUNDS_FOLDER, number, soundFile);
	}

	public Sound(String name, int number, String soundFile, Integer alertId) {
		this(name, null, number, soundFile, alertId);
	}

	public Sound(String name, String folder, int number, String soundFile) {
		this(name, folder, number, soundFile, null);
	}

	public Sound(String name, String folder, int number, String soundFile, Integer alertId) {
		this.name = name;
		this.soundFile = soundFile;
		id = number;
		this.alertId = alertId;

		path = Config.WORKING_FOLDER + folder + number + '/';
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();

		sb.append(getId());
		if (isNamed()) {
			sb.append(" (");
			sb.append(getName());
			sb.append(")");
		}

		return sb.toString();
	}

	public String getNameNotNull() {
		return isNamed() ? name : "";
	}

	public String getAlertIdNotNull() {
		return alertId != null ? alertId.toString() : "";
	}

	public boolean isUnnamed() {
		return name == null || name.trim().isEmpty();
	}

	public boolean isNamed() {
		return !isUnnamed();
	}

	public int getId() {
		return id;
	}

	public void setId(int number) {
		id = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public String getSoundFile() {
		return soundFile;
	}

	public void setSoundFile(String soundFile) {
		this.soundFile = soundFile;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getAlertId() {
		return alertId;
	}

	public void setAlertId(Integer alertId) {
		this.alertId = alertId;
	}

	public void setAlertId(String alertId) {
		if (alertId != null) {
			this.alertId = Integer.parseInt(alertId);
		} else {
			alertId = null;
		}
	}

	@Override
	public boolean equals(Object obj) {
		Sound s = (Sound) obj;
		return id == s.id && path.equals(s.path);
	}

	public String getTextForDisplay() {
		if (isNamed()) {
			return name;
		}
		return Config.TEXT_SOUND + " " + id;
	}
}
