package com.audioant.audio.learning;

import java.io.File;

import com.audioant.audio.model.SoundModel;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */

public class SoundFileLearner extends SoundLearner {

	public SoundFileLearner(String pathname) {
		super(new SoundModel(""));
		super.soundfile = pathname;
		File file = new File(pathname);
		file.getParentFile().mkdirs();
	}
}
