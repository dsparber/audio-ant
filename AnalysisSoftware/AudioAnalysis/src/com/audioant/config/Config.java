package com.audioant.config;

import java.text.SimpleDateFormat;

import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;

/**
 *
 * @author Daniel Sparber
 * @year 2016
 *
 * @version 1.1
 */

public class Config {

	// Working directory
	public static final String WORKING_FOLDER = ConfigReader.getProperty("workingFolder");

	// Learned Sounds
	public static final String LEARNED_SOUNDS_FOLDER = ConfigReader.getProperty("learnedSoundsFolder");
	public static final String LEARNED_SOUNDS_FOLDER_PATH = WORKING_FOLDER + LEARNED_SOUNDS_FOLDER;

	public static final String LEARNED_SOUNDS_FILE = ConfigReader.getProperty("learnedSoundsFileSound");

	public static final String LEARNED_SOUNDS_FILE_FREQUENCIES = ConfigReader
			.getProperty("learnedSoundsFileFrequencies");
	public static final String LEARNED_SOUNDS_FILE_SRP = ConfigReader.getProperty("learnedSoundsFileSrp");
	public static final String LEARNED_SOUNDS_FILE_MFCC = ConfigReader.getProperty("learnedSoundsFileMfcc");
	public static final String LEARNED_SOUNDS_FILE_POWER = ConfigReader.getProperty("learnedSoundsFilePower");

	// Learned Sounds XML
	public static final String LEARNED_SOUNDS_XML_FILE = ConfigReader.getProperty("learndedSoundsXml");
	public static final String LEARNED_SOUNDS_XML_FILE_PATH = LEARNED_SOUNDS_FOLDER_PATH + LEARNED_SOUNDS_XML_FILE;

	public static final String LEARNED_SOUNDS_XML_ROOT = ConfigReader.getProperty("learndedSoundsXmlRoot");
	public static final String LEARNED_SOUNDS_XML_SOUND = ConfigReader.getProperty("learndedSoundsXmlSound");
	public static final String LEARNED_SOUNDS_XML_NAMED = ConfigReader.getProperty("learndedSoundsXmlNamed");
	public static final String LEARNED_SOUNDS_XML_NUMBER = ConfigReader.getProperty("learndedSoundsXmlNumber");
	public static final String LEARNED_SOUNDS_XML_NAME = ConfigReader.getProperty("learndedSoundsXmlName");
	public static final String LEARNED_SOUNDS_XML_PATH = ConfigReader.getProperty("learndedSoundsXmlPath");

	// Hardware controller
	public static final int HW_CONTROLLER_PORT = ConfigReader.getPropertyAsInt("hardwareControllerPort");
	public static final String HW_CONTROLLER_HOST = ConfigReader.getProperty("hardwareControllerHost");
	public static final char HW_CONTROLLER_SEP = ConfigReader.getPropertyAsChar("hardwareControllerValueSeperator");
	public static final String HW_CONTROLLER_FOLDER = ConfigReader.getProperty("hardwareControllerFolder");
	public static final String HW_CONTROLLER_COMMAND = ConfigReader.getProperty("hardwareControllerCommand");
	public static final String HW_CONTROLLER_MAIN_FILE = ConfigReader.getProperty("hardwareControllerMainFile");
	public static final String HW_CONTROLLER_EXECUTE = HW_CONTROLLER_COMMAND + " " + HW_CONTROLLER_MAIN_FILE;

	// Audio parameters

	public static final float AUDIO_SAMPLE_RATE = ConfigReader.getPropertyAsFloat("audioSampleRate");
	public static final int AUDIO_SAMPLE_SIZE = ConfigReader.getPropertyAsInt("audioSampleSize");
	public static final int AUDIO_CHANNELS = ConfigReader.getPropertyAsInt("audioChannels");
	public static final boolean AUDIO_BIG_ENDIAN = ConfigReader.getPropertyAsBoolean("audioBigEndian");
	public static final boolean AUDIO_SIGNED = ConfigReader.getPropertyAsBoolean("audioSigned");

	private static final String AUDIO_TYPE_NAME = ConfigReader.getProperty("audioTypeName");
	private static final String AUDIO_TYPE_EXTENSION = ConfigReader.getProperty("audioTypeExtension");
	public static final Type AUDIO_TYPE = new Type(AUDIO_TYPE_NAME, AUDIO_TYPE_EXTENSION);

	public static final String AUDIO_FILE_PATTERN = ConfigReader.getProperty("audioFilePattern");
	public static final String AUDIO_FILE_PATTERN_WAV = ConfigReader.getProperty("audioFilePatternWav");
	public static final String AUDIO_FILE_PATTERN_MP3 = ConfigReader.getProperty("audioFilePatternMp3");

	public static final AudioFormat AUDIO_FORMAT = new AudioFormat(AUDIO_SAMPLE_RATE, AUDIO_SAMPLE_SIZE, AUDIO_CHANNELS,
			AUDIO_SIGNED, AUDIO_BIG_ENDIAN);

	public static final int AUDIO_WINDOW_SIZE = ConfigReader.getPropertyAsInt("audioWindowSize");

	// Audio analysis parameter

	// For strongest frequency analysis
	public static final double AUDIO_ANALYSIS_FREQUENCY_THRESHOLD = ConfigReader
			.getPropertyAsDouble("audioAnalysisFrequencyThreshold");
	public static final double AUDIO_ANALYSIS_FREQUENCY_THRESHOLD_AMPLITUDE = ConfigReader
			.getPropertyAsDouble("audioAnalysisFrequencyThresholdAmplitude");
	public static final int AUDIO_ANALYSIS_FREQUENCY_MAX_PEAK_COUNT = ConfigReader
			.getPropertyAsInt("audioAnalysisFrequencyMaxPeakCount");
	public static final int AUDIO_ANALYSIS_FREQUENCY_MIN = ConfigReader.getPropertyAsInt("audioAnalysisFrequencyMin");
	public static final int AUDIO_ANALYSIS_FREQUENCY_MAX = ConfigReader.getPropertyAsInt("audioAnalysisFrequencyMax");

	public static final int AUDIO_ANALYSIS_BANDPASS_DELTA = ConfigReader.getPropertyAsInt("audioAnalysisBandpassDelta");

	// For MFCC analysis
	private static final boolean MFCC_U0 = ConfigReader.getPropertyAsBoolean("audioAnalysisMfccCoefficientsUsed0");
	private static final boolean MFCC_U1 = ConfigReader.getPropertyAsBoolean("audioAnalysisMfccCoefficientsUsed1");
	private static final boolean MFCC_U2 = ConfigReader.getPropertyAsBoolean("audioAnalysisMfccCoefficientsUsed2");
	private static final boolean MFCC_U3 = ConfigReader.getPropertyAsBoolean("audioAnalysisMfccCoefficientsUsed3");
	private static final boolean MFCC_U4 = ConfigReader.getPropertyAsBoolean("audioAnalysisMfccCoefficientsUsed4");
	private static final boolean MFCC_U5 = ConfigReader.getPropertyAsBoolean("audioAnalysisMfccCoefficientsUsed5");
	private static final boolean MFCC_U6 = ConfigReader.getPropertyAsBoolean("audioAnalysisMfccCoefficientsUsed6");
	private static final boolean MFCC_U7 = ConfigReader.getPropertyAsBoolean("audioAnalysisMfccCoefficientsUsed7");
	private static final boolean MFCC_U8 = ConfigReader.getPropertyAsBoolean("audioAnalysisMfccCoefficientsUsed8");
	private static final boolean MFCC_U9 = ConfigReader.getPropertyAsBoolean("audioAnalysisMfccCoefficientsUsed9");
	private static final boolean MFCC_U10 = ConfigReader.getPropertyAsBoolean("audioAnalysisMfccCoefficientsUsed10");
	private static final boolean MFCC_U11 = ConfigReader.getPropertyAsBoolean("audioAnalysisMfccCoefficientsUsed11");

	private static final double MFCC_T0 = ConfigReader.getPropertyAsDouble("audioAnalysisMfccTolerance0");
	private static final double MFCC_T1 = ConfigReader.getPropertyAsDouble("audioAnalysisMfccTolerance1");
	private static final double MFCC_T2 = ConfigReader.getPropertyAsDouble("audioAnalysisMfccTolerance2");
	private static final double MFCC_T3 = ConfigReader.getPropertyAsDouble("audioAnalysisMfccTolerance3");
	private static final double MFCC_T4 = ConfigReader.getPropertyAsDouble("audioAnalysisMfccTolerance4");
	private static final double MFCC_T5 = ConfigReader.getPropertyAsDouble("audioAnalysisMfccTolerance5");
	private static final double MFCC_T6 = ConfigReader.getPropertyAsDouble("audioAnalysisMfccTolerance6");
	private static final double MFCC_T7 = ConfigReader.getPropertyAsDouble("audioAnalysisMfccTolerance7");
	private static final double MFCC_T8 = ConfigReader.getPropertyAsDouble("audioAnalysisMfccTolerance8");
	private static final double MFCC_T9 = ConfigReader.getPropertyAsDouble("audioAnalysisMfccTolerance9");
	private static final double MFCC_T10 = ConfigReader.getPropertyAsDouble("audioAnalysisMfccTolerance10");
	private static final double MFCC_T11 = ConfigReader.getPropertyAsDouble("audioAnalysisMfccTolerance11");

	public static final boolean[] AUDIO_ANALYSIS_MFCC_COEFFICIENT_USED = { MFCC_U0, MFCC_U1, MFCC_U2, MFCC_U3, MFCC_U4,
			MFCC_U5, MFCC_U6, MFCC_U7, MFCC_U8, MFCC_U9, MFCC_U10, MFCC_U11 };

	public static final double[] AUDIO_ANALYSIS_MFCC_TOLERANCE = { MFCC_T0, MFCC_T1, MFCC_T2, MFCC_T3, MFCC_T4, MFCC_T5,
			MFCC_T6, MFCC_T7, MFCC_T8, MFCC_T9, MFCC_T10, MFCC_T11 };

	// For energy analysis
	public static final int AUDIO_ANALYSIS_POWER_MIN_RMS = ConfigReader.getPropertyAsInt("audioAnalysisEnergyMinRms");
	public static final double AUDIO_ANALYSIS_ENERGY_TOLERANCE = ConfigReader
			.getPropertyAsDouble("audioAnalysisEnergyTolerance");

	// For SRP analysis
	public static final double AUDIO_ANALYSIS_SRP_BORDER = ConfigReader.getPropertyAsDouble("audioAnalysisSrpBorder");
	public static final double AUDIO_ANALYSIS_SRP_TOLERANCE = ConfigReader
			.getPropertyAsDouble("audioAnalysisSrpTolerance");

	// Detection thresholds
	public static final double AUDIO_ANALYSIS_MATCH_THRESHOLD_SRP = ConfigReader
			.getPropertyAsDouble("audioAnalysisMatchThresholdSrp");
	public static final double AUDIO_ANALYSIS_MATCH_THRESHOLD_MFCC = ConfigReader
			.getPropertyAsDouble("audioAnalysisMatchThresholdMfcc");
	public static final double AUDIO_ANALYSIS_MATCH_THRESHOLD_ENERGY = ConfigReader
			.getPropertyAsDouble("audioAnalysisMatchThresholdEnergy");
	public static final double AUDIO_ANALYSIS_MATCH_THRESHOLD_FREQUENCY = ConfigReader
			.getPropertyAsDouble("audioAnalysisMatchThresholdFrequency");

	public static final double AUDIO_ANALYSIS_MATCH_THRESHOLD_MIN_SRP = ConfigReader
			.getPropertyAsDouble("audioAnalysisMatchThresholdMinSrp");
	public static final double AUDIO_ANALYSIS_MATCH_THRESHOLD_MIN_MFCC = ConfigReader
			.getPropertyAsDouble("audioAnalysisMatchThresholdMinMfcc");
	public static final double AUDIO_ANALYSIS_MATCH_THRESHOLD_MIN_ENERGY = ConfigReader
			.getPropertyAsDouble("audioAnalysisMatchThresholdMinEnergy");
	public static final double AUDIO_ANALYSIS_MATCH_THRESHOLD_MIN_FREQUENCY = ConfigReader
			.getPropertyAsDouble("audioAnalysisMatchThresholdMinFrequency");

	// Detetion weigths
	public static final double AUDIO_ANALYSIS_MATCH_WEIGHT_SRP = ConfigReader
			.getPropertyAsDouble("audioAnalysisMatchWeightSrp");
	public static final double AUDIO_ANALYSIS_MATCH_WEIGHT_MFCC = ConfigReader
			.getPropertyAsDouble("audioAnalysisMatchWeightMfcc");
	public static final double AUDIO_ANALYSIS_MATCH_WEIGHT_ENERGY = ConfigReader
			.getPropertyAsDouble("audioAnalysisMatchWeightEnergy");
	public static final double AUDIO_ANALYSIS_MATCH_WEIGHT_FREQUENCY = ConfigReader
			.getPropertyAsDouble("audioAnalysisMatchWeightFrequency");
	public static final double AUDIO_ANALYSIS_MATCH_WEIGHT_THRESHOLD = ConfigReader
			.getPropertyAsDouble("audioAnalysisMatchWeightThreshold");

	// Automated test
	public static final String AUTOTEST_PATTERN_TRUE = ConfigReader.getProperty("automatedTestFilePatternTrue");
	public static final String AUTOTEST_PATTERN_REF = ConfigReader.getProperty("automatedTestFilePatternReference");

	// Database parameters
	public static final String DATABASE_USER = ConfigReader.getProperty("databaseUser");
	public static final String DATABASE_PASSWORD = ConfigReader.getProperty("databasePassword");
	public static final String DATABASE_HOST = ConfigReader.getProperty("databaseHost");

	public static final String DATABASE_TEST = ConfigReader.getProperty("databaseTest");

	// CSV parameters
	public static final char CSV_SEPERATOR_TEXT = ConfigReader.getPropertyAsChar("csvSeperatorText");;
	public static final char CSV_SEPERATOR_VALUE = ConfigReader.getPropertyAsChar("csvSeperatorValue");;
	public static final String CSV_SEPERATOR = ConfigReader.getProperty("csvSeperator");;

	// Date formats
	private static final String STRING_DATE_FULL_NO_SPACE = ConfigReader.getProperty("dateFormatFullNoSpace");
	private static final String STRING_DATE_FULL_MILLIS = ConfigReader.getProperty("dateFormatFullMillis");
	public static final SimpleDateFormat DATE_FORMAT_FULL_NO_SPACE = new SimpleDateFormat(STRING_DATE_FULL_NO_SPACE);
	public static final SimpleDateFormat DATE_FORMAT_FULL_MILLIS = new SimpleDateFormat(STRING_DATE_FULL_MILLIS);

	// Logging
	public static final String LOG_FOLDER = ConfigReader.getProperty("logFolder");
	public static final String LOG_FOLDER_EVENTS = ConfigReader.getProperty("logFolderEvents");
	public static final String LOG_FOLDER_DETAIL = ConfigReader.getProperty("logFolderDetail");
	public static final String LOG_FOLDER_ERROR = ConfigReader.getProperty("logFolderError");
	public static final String LOG_FOLDER_EVENTS_PATH = WORKING_FOLDER + LOG_FOLDER + LOG_FOLDER_EVENTS;
	public static final String LOG_FOLDER_DETAIL_PATH = WORKING_FOLDER + LOG_FOLDER + LOG_FOLDER_DETAIL;
	public static final String LOG_FOLDER_ERROR_PATH = WORKING_FOLDER + LOG_FOLDER + LOG_FOLDER_ERROR;
	public static final String LOG_SUFFIX = ConfigReader.getProperty("logSuffix");

	// String formats
	public static final String STRING_FORMATTER_DOUBLE = ConfigReader.getProperty("stringFormatterDouble");

	// Android
	public static final int ANDROID_PORT = ConfigReader.getPropertyAsInt("androidPort");
}