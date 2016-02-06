package com.audioant.test.model;

import com.audioant.config.Config;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class ResultModel {

	private int id;
	private int testId;
	private int soundId;

	private String fileName;

	private boolean shouldBeRecognised;
	private boolean wasRecognised;
	private boolean correctRecognition;

	public ResultModel(int testId, int soundId, String fileName, boolean wasRecognised) {
		this.testId = testId;
		this.soundId = soundId;
		this.fileName = fileName;
		this.wasRecognised = wasRecognised;

		shouldBeRecognised = fileName.matches(Config.AUTOTEST_PATTERN_TRUE);

		correctRecognition = (this.wasRecognised == shouldBeRecognised);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTestId() {
		return testId;
	}

	public int getSoundId() {
		return soundId;
	}

	public String getFileName() {
		return fileName;
	}

	public boolean isShouldBeRecognised() {
		return shouldBeRecognised;
	}

	public boolean isWasRecognised() {
		return wasRecognised;
	}

	public boolean isCorrectRecognition() {
		return correctRecognition;
	}
}
