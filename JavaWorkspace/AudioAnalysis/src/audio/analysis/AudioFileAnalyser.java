package audio.analysis;

import java.io.IOException;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RserveException;

import audio.parameters.AudioParamters;
import audio.windowing.Windowing;
import io.wave.WavAudioFileReader;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class AudioFileAnalyser extends AudioAnalyser {

	private String pathname;

	public AudioFileAnalyser(String pathname) throws RserveException, IOException {
		super();
		this.pathname = pathname;
	}

	public double getMaxMatch() throws IOException, REngineException, REXPMismatchException {

		WavAudioFileReader reader = new WavAudioFileReader(pathname);

		float sampleRate = reader.getWaveFormat().getSamplesPerSec();

		int[][] windows = Windowing.createWindows(reader.readData(), AudioParamters.WINDOW_SIZE, 0f);

		double max = 0;

		for (int[] samples : windows) {

			addRecentFreq(samples, sampleRate);
			double currentMatch = getFreqMatch();

			if (currentMatch > max) {
				max = currentMatch;
			}
		}
		return max;
	}
}
