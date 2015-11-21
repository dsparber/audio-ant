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

	private static final char LEARN_OPTION = 'n';
	private static final char START_STREAM_OPTION = 's';
	private static final char STOP_STREAM_OPTION = 'e';
	private static final char QUIT_OPTION = 'q';

	public static void main(String[] args) {

		printUsage();

		Scanner s = new Scanner(System.in);

		AnalyseStream analyse = null;

		char option;
		while ((option = s.nextLine().charAt(0)) != QUIT_OPTION) {
			if (option == LEARN_OPTION) {

				try {
					new LearnSound().learnSound();
				} catch (LineUnavailableException | IOException | REngineException | REXPMismatchException e) {
					e.printStackTrace();
				}

			} else if (option == START_STREAM_OPTION) {

				try {
					analyse = new AnalyseStream();
					analyse.start();
				} catch (RserveException | IOException e) {
					e.printStackTrace();
				}

			} else if (option == STOP_STREAM_OPTION) {

				try {
					analyse.stop();
				} catch (NullPointerException e) {
					System.out.println("Audio analysis never started");
				}

			} else {
				System.out.println("Invalid Option");
			}

		}
		s.close();
	}

	private static void printUsage() {
		System.out.println("Usage:");
		System.out.println(LEARN_OPTION + ": learn new Signal");
		System.out.println(START_STREAM_OPTION + ": start analysing stream");
		System.out.println(STOP_STREAM_OPTION + ": stop the analysis");
		System.out.println(QUIT_OPTION + ": quit program");
	}
}
