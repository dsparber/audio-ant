package com.audioant.audio.model;

import com.audioant.config.Config;

public class Sound {

	private int id;
	private String name;
	private String path;
	private Integer alertId;

	public Sound() {
	}

	public Sound(String name, int number) {
		this(name, Config.LEARNED_SOUNDS_FOLDER, number);
	}

	public Sound(String name, String folder, int number) {
		this(name, folder, number, null);
	}

	public Sound(String name, String folder, int number, Integer alertId) {
		this.name = name;
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
