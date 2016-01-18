package com.audioant.audio.learning;

import java.io.File;

import com.audioant.audio.model.Sound;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */

public class SoundFileLearner extends SoundLearner {

	public SoundFileLearner(String pathname) {
		super(new Sound());
		super.soundfile = pathname;
		File file = new File(pathname);
		file.getParentFile().mkdirs();
	}
}
