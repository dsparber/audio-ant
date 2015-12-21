package audio.learning;

import java.io.File;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */

public class SoundFileLearner extends SoundLearner {

	public SoundFileLearner(String pathname) {
		super.pathnameIn = pathname;
		File file = new File(pathname);
		file.getParentFile().mkdirs();
	}
}
