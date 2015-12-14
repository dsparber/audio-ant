package audio.learning;

import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;

import audio.analysis.WindowAnalyser;
import audio.windowing.Windowing;
import config.Parameters.Audio;
import config.Parameters.Audio.Analysis;
import config.Parameters.WorkingDir;
import io.audio.AudioFileReader;
import io.audio.AudioFileReaderFactory;
import io.csv.CsvWriter;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */

public abstract class SoundLearner {

	protected String pathnameIn;
	protected String pathnameOut = WorkingDir.FOLDER_LEARNED_SOUNDS + WorkingDir.FEATURES_CSV;

	public void extractFeatures() throws LineUnavailableException, IOException, REngineException, REXPMismatchException,
			UnsupportedAudioFileException {

		Double[] strongestFreq = extractStrongestFrequency();

		CsvWriter writer = new CsvWriter(pathnameOut);
		writer.writeSingleColmn(strongestFreq);
	}

	protected Double[] extractStrongestFrequency()
			throws IOException, REngineException, REXPMismatchException, UnsupportedAudioFileException {

		AudioFileReader reader = AudioFileReaderFactory.getFileReader(pathnameIn);

		int[][] windows = Windowing.createWindows(reader.readData(), Audio.WINDOW_SIZE, 0f);

		ArrayList<Double> results = new ArrayList<Double>();

		for (int samples[] : windows) {

			float sampleRate = reader.getSampleRate();

			WindowAnalyser analyser = new WindowAnalyser();
			analyser.assignSamples(samples, sampleRate);

			double strongestFreq = analyser.getStrongestFrequency();
			double energy = analyser.getEnergy();

			if (energy > Analysis.MIN_ENERGY && strongestFreq >= Analysis.MIN_FREQ
					&& strongestFreq <= Analysis.MAX_FREQ) {
				results.add(strongestFreq);
			}

		}

		Double[] result = new Double[results.size()];
		for (int j = 0; j < results.size(); j++) {
			result[j] = results.get(j);
		}

		return result;
	}
}
