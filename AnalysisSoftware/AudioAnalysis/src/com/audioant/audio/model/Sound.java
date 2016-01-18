package com.audioant.audio.model;

import com.audioant.config.Parameters.WorkingDir;

public class Sound {

	private int number;
	private String name;
	private String path;

	public Sound() {
	}

	public Sound(String name, int number) {
		this.name = name;
		this.number = number;

		path = WorkingDir.FOLDER_LEARNED_SOUNDS + number + '/';
	}

	public String getNameNotNull() {
		return isNamed() ? name : "";
	}

	public boolean isUnnamed() {
		return name == null;
	}

	public boolean isNamed() {
		return name != null;
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
