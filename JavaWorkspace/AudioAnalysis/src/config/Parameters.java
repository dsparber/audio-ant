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

	// Working directory parameters
	public static class WorkingDir {

		public static final String FOLDER = "res/";
		public static final String FOLDER_LEARNED_SOUNDS = FOLDER + "learnedSounds/";
		public static final String AUDIO_FILE = "out.wav";
		public static final String FEATURES_CSV = "features.csv";
	}

	// Communication parameters
	public static class Communication {

		public static final int RASPBERRY_SOCKET_PORT = 4207;
	}

	// Audio parameters
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

		// Audio analysis parameter
		public static class Analysis {

			public static final double DETECTION_THRESHOLD = 5; // Hertz
			public static final double MATCH_THRESHOLD = .75; // Percent

			public static final int MIN_FREQ = 500;
			public static final int MAX_FREQ = 8000;

			public static final int MIN_ENERGY = 40;
		}
	}

	// Database parameters
	public static class Database {

		public static final String USER = "audioant";
		public static final String PASSWORD = "audioant";

		public static final String DATABASE_TEST = "audioantTest";
		public static final String HOST = "localhost";
	}

	// Automated test paramaters
	public static class AutomatedTest {

		public static final String NAME_PATTERN_SOUND_FILE = ".*.(wav|mp3)";

		public static final String NAME_PATTERN_SHOULD_BE_RECOGNISED = "true.*.(wav|mp3)";
		public static final String NAME_PATTERN_REFERENCE_FILE = "00.*.(wav|mp3)";
	}
}