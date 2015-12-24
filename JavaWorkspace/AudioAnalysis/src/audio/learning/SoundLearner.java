package audio.learning;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;

import audio.analysis.WindowAnalyser;
import audio.analysis.model.StrongestFrequenciesModel;
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

		List<StrongestFrequenciesModel> strongestFreq = extractStrongestFrequency();

		int maxWidth = 0;

		for (StrongestFrequenciesModel model : strongestFreq) {
			if (model.size() > maxWidth) {
				maxWidth = model.size();
			}
		}

		String[][] out = new String[strongestFreq.size()][maxWidth];

		for (int i = 0; i < out.length; i++) {
			for (int j = 0; j < out[i].length; j++) {

				if (strongestFreq.get(i).size() > j) {
					out[i][j] = strongestFreq.get(i).get(j).toString();
				} else {
					out[i][j] = "";
				}
			}
		}

		CsvWriter writer = new CsvWriter(pathnameOut);
		writer.writeMatrix(out);
	}

	protected List<StrongestFrequenciesModel> extractStrongestFrequency()
			throws IOException, REngineException, REXPMismatchException, UnsupportedAudioFileException {

		AudioFileReader reader = AudioFileReaderFactory.getFileReader(pathnameIn);

		int[][] windows = Windowing.createWindows(reader.readData(), Audio.WINDOW_SIZE, 0f);

		ArrayList<StrongestFrequenciesModel> results = new ArrayList<StrongestFrequenciesModel>();

		for (int samples[] : windows) {

			float sampleRate = reader.getSampleRate();

			WindowAnalyser analyser = new WindowAnalyser();
			analyser.assignSamples(samples, sampleRate);

			double energy = analyser.getEnergy();

			if (energy > Analysis.MIN_ENERGY) {

				StrongestFrequenciesModel strongestFreq = analyser.getStrongestFrequencies();

				if (strongestFreq.size() > 0 && strongestFreq.size() <= Analysis.MAX_PEAK_COUNT) {
					results.add(strongestFreq);
				}
			}
		}
		return results;
	}
}
