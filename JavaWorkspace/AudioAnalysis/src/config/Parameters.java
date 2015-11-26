package config;

import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */

public class Parameters {

	public static class WorkingDir {

		public static final String FOLDER = "res/";
		public static final String FOLDER_LEARNED_SOUNDS = FOLDER + "learnedSounds/";
		public static final String AUDIO_FILE = "out.wav";
		public static final String FEATURES_CSV = "features.csv";
	}

	public static class Audio {

		public static final float SAMPLE_RATE = 16000;
		public static final int SAMPLE_SIZE_IN_BITS = 16;
		public static final int CHANNELS = 1;
		public static final boolean BIG_ENDIAN = false;
		public static final boolean SIGNED = true;
		public static final Type FILE_TYPE = Type.WAVE;

		public static final AudioFormat AUDIO_FORMAT = new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE_IN_BITS, CHANNELS,
				SIGNED, BIG_ENDIAN);

		public static final int WINDOW_SIZE = 2048;

		public static class Analysis {

			public static final double DETECTION_THRESHOLD = 5; // Hertz

			public static final int MIN_FREQ = 500;
			public static final int MAX_FREQ = 5000;
		}
	}
}