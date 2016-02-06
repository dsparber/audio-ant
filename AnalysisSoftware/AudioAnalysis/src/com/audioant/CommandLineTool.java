package com.audioant;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RserveException;

import com.audioant.audio.analysis.AudioStreamAnalyser;
import com.audioant.audio.learning.LearnedSounds;
import com.audioant.audio.learning.MicrophoneSoundLearner;
import com.audioant.audio.model.Sound;
import com.audioant.io.eventObserver.EventLogger;
import com.audioant.test.FeatureTest;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class CommandLineTool {

	private static final char LEARN_OPTION = 'l';
	private static final char ANALYSE_STREAM_OPTION = 'a';
	private static final char END_STREAM_ANALYSIS_OPTION = 'e';
	private static final char QUIT_OPTION = 'q';
	private static final char AUTO_TEST = 't';

	private static AudioStreamAnalyser analyser;

	public static void start() {

		printUsage();

		Scanner s = new Scanner(System.in);

		char option;
		while ((option = readSingelChar(s)) != QUIT_OPTION) {

			if (option == LEARN_OPTION) {

				learnSound(s);

			} else if (option == ANALYSE_STREAM_OPTION) {

				analyseStream();

			} else if (option == END_STREAM_ANALYSIS_OPTION) {

				endAnalysing();

			} else if (option == AUTO_TEST) {

				performAutoTest(s);

			} else {
				System.out.println("Invalid Option");
				printUsage();
			}
		}
		s.close();
		System.out.println("Program terminated");
	}

	private static void printUsage() {
		System.out.println("Usage:");
		System.out.println(LEARN_OPTION + ": learn new Signal");
		System.out.println(ANALYSE_STREAM_OPTION + ": analyse stream");
		System.out.println(END_STREAM_ANALYSIS_OPTION + ": end the analysing process");
		System.out.println(AUTO_TEST + ": start an automated test");
		System.out.println(QUIT_OPTION + ": quit program");
	}

	private static void analyseStream() {
		try {
			EventLogger logger = new EventLogger();
			analyser = new AudioStreamAnalyser();
			analyser.addObserver(logger);
			analyser.start();
			System.out.println("Analysing stream");
		} catch (RserveException | IOException e) {
			e.printStackTrace();
		}
	}

	private static void endAnalysing() {
		try {
			analyser.stop();
			System.out.println("Analysing stopped");
			System.out.printf("Avg receive time: %.2f ms\n", analyser.getAvgSampleReceiveTime());
			System.out.printf("Avg analysis time: %.2f ms\n", analyser.getAvgAnalysisTime());

		} catch (NullPointerException e) {
			System.out.println("Audio analysis never started");
		}
	}

	private static void learnSound(Scanner scanner) {

		try {
			System.out.print("Enter the name of the sound: ");

			Sound sound = LearnedSounds.getNewSound(scanner.nextLine());

			MicrophoneSoundLearner learnSound = new MicrophoneSoundLearner(sound);

			System.out.println("Hit return to start recording");
			scanner.nextLine();

			learnSound.startCapturing();
			System.out.println("Start recording (Hit return to end)");

			scanner.nextLine();
			learnSound.stopCapturing();
			System.out.println("Recording ended");
			System.out.print("Extracting features...");

			learnSound.extractFeatures();

			LearnedSounds.addSound(sound);
			LearnedSounds.saveSounds();
			System.out.println("done");

		} catch (LineUnavailableException | IOException | REngineException | REXPMismatchException
				| UnsupportedAudioFileException | ParserConfigurationException | TransformerException
				| IndexOutOfBoundsException e) {
			System.out.println("failed");
			e.printStackTrace();
		}

	}

	private static void performAutoTest(Scanner scanner) {

		try {
			System.out.print("Testverzeichnis eingeben: ");
			String dir = scanner.nextLine();
			System.out.print("Dateiformat eingeben: ");
			String type = scanner.nextLine();
			FeatureTest test = new FeatureTest(dir, type);

			System.out.println("Performing test...");
			test.analyseFiles();
			System.out.println("done");
		} catch (LineUnavailableException | IOException | REngineException | REXPMismatchException | SQLException
				| UnsupportedAudioFileException e) {
			System.out.println("failed");
			e.printStackTrace();
		}
	}

	private static char readSingelChar(Scanner scanner) {
		String in = scanner.nextLine();

		if (in.length() > 0) {
			return in.charAt(0);
		}
		return ' ';
	}
}
