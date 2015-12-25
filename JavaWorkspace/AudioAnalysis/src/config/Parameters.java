package config;

import java.text.SimpleDateFormat;

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
		public static final String AUDIO_FILE = FOLDER_LEARNED_SOUNDS + "learnedSound.wav";
		public static final String FREQUENCIES_CSV = FOLDER_LEARNED_SOUNDS + "frequencies.csv";
		public static final String SRP_CSV = FOLDER_LEARNED_SOUNDS + "srp.csv";
	}

	// Communication parameters
	public static class Communication {

		public static final int RASPBERRY_SOCKET_PORT = 4207;
		// public static final String RASPBERRY_HOST_NAME = "audiopi.local";
		public static final String RASPBERRY_HOST_NAME = "localhost";
		public static final char VALUE_SEPERATOR = ';';
	}

	// Python resources
	public static class PythonResources {

		public static final String FOLDER = "../../RaspberryWorkspace/HardwareController/";

		public static final String EXECUTE = "sudo python3 ";
		public static final String CONNECTION_PY = FOLDER + "connection.py";
	}

	// CSV parameters
	public static class Csv {
		public static final char TEXT_SEPERATOR = '"';
		public static final char VALUE_SEPERATOR = ',';
		public static final String SEPERATOR = "" + TEXT_SEPERATOR + VALUE_SEPERATOR + TEXT_SEPERATOR;
	}

	// Date formats
	public static class DateFormat {

		public static final SimpleDateFormat FULL_DATE = new SimpleDateFormat("yyyyMMdd_HHmmss");
		public static final SimpleDateFormat FULL_DATE__MILLIS_SEPERATED = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss:SSS");
	}

	// Logging
	public static class Logging {

		public static final String LOG_FOLDER = "res/log/";
		public static final String LOG_FOLDER_EVENTS = LOG_FOLDER + "events/";
		public static final String LOG_SUFFIX = ".log";
	}

	// String formats
	public static class StringFormatter {

		public static final String DOUBLE_FORMAT = "%,.2f";
		public static final String STRING_PLACEHOLDER = "%s";
	}

	// Special chars
	public static class SpecialChars {

		public static final char NEW_LINE = '\n';
	}

	// Audio parameters
	public static class Audio {

		public static final float SAMPLE_RATE = 16000;
		public static final int SAMPLE_SIZE_IN_BITS = 16;
		public static final int CHANNELS = 1;
		public static final boolean BIG_ENDIAN = false;
		public static final boolean SIGNED = true;
		public static final Type FILE_TYPE = Type.WAVE;

		public static final String WAV_FILEPATTERN = "wav";
		public static final String MP3_FILEPATTERN = "mp3";

		public static final AudioFormat AUDIO_FORMAT = new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE_IN_BITS, CHANNELS,
				SIGNED, BIG_ENDIAN);

		public static final int WINDOW_SIZE = 2048;

		// Audio analysis parameter
		public static class Analysis {

			public static final double DETECTION_THRESHOLD = 5; // Hertz
			public static final double STRONGEST_FREQUENCY_MATCH_THRESHOLD = .7; // Percent
			public static final double SRP_MATCH_THRESHOLD = .33; // Percent

			public static final double AMPLITUDE_THRESHOLD = 0.1;

			public static final int MIN_FREQ = 500;
			public static final int MAX_FREQ = 8000;

			public static final int MIN_ENERGY = 40;
			public static final int MAX_PEAK_COUNT = 4;

			public static final double SRP_TOLERANCE = .04; // Percent
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