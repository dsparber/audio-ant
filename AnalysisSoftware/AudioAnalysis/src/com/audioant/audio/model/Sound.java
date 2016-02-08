package com.audioant.audio.model;

import com.audioant.config.Config;

public class Sound {

	private int number;
	private String name;
	private String path;

	public Sound() {
	}

	public Sound(String name, int number) {
		this.name = name;
		this.number = number;

		path = Config.WORKING_FOLDER + Config.LEARNED_SOUNDS_FOLDER + number + '/';
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();

		sb.append(getNumber());
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

	public boolean isUnnamed() {
		return name == null || name.trim().isEmpty();
	}

	public boolean isNamed() {
		return !isUnnamed();
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
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
}
