import java.io.IOException;
import java.util.Scanner;

import javax.sound.sampled.LineUnavailableException;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RserveException;

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

	private static AnalyseStream analyse;

	public static void main(String[] args) {

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
		System.out.println(QUIT_OPTION + ": quit program");
	}

	private static void analyseStream() {
		try {
			EventLogger logger = new EventLogger();
			analyse = new AnalyseStream();
			analyse.addObserver(logger);
			analyse.start();
			System.out.println("Analysing stream");
		} catch (RserveException | IOException e) {
			e.printStackTrace();
		}
	}

	private static void endAnalysing() {
		try {
			analyse.stop();
			System.out.println("Analysing stopped");
		} catch (NullPointerException e) {
			System.out.println("Audio analysis never started");
		}
	}

	private static void learnSound(Scanner scanner) {

		try {
			LearnSound learnSound = new LearnSound();

			System.out.println("Hit return to start recording");
			scanner.nextLine();

			learnSound.startCapturing();
			System.out.println("Start recording (Hit return to end)");

			scanner.nextLine();
			learnSound.stopCapturing();
			System.out.println("Recording ended");
			System.out.print("Extracting features...");

			learnSound.extractFeatures();
			System.out.println("done");

		} catch (LineUnavailableException | IOException | REngineException | REXPMismatchException e) {
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
