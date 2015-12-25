package audio.analysis;

import java.io.IOException;

import javax.sound.sampled.UnsupportedAudioFileException;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RserveException;

import audio.windowing.Windowing;
import config.Parameters.Audio;
import io.audio.AudioFileReader;
import io.audio.AudioFileReaderFactory;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class AudioFileAnalyser extends AudioAnalyser {

	private String pathname;

	private double match, frequencyMatch, srpMatch;

	public AudioFileAnalyser(String pathname) throws RserveException, IOException {
		super();
		this.pathname = pathname;
	}

	public void analyse() throws IOException, REngineException, REXPMismatchException, UnsupportedAudioFileException {

		AudioFileReader reader = AudioFileReaderFactory.getFileReader(pathname);

		float sampleRate = reader.getSampleRate();

		int[][] windows = Windowing.createWindows(reader.readData(), Audio.WINDOW_SIZE, 0f);

		double max = 0;
		double maxSrp = 0;
		double maxFreq = 0;

		for (int[] samples : windows) {

			addSamples(samples, sampleRate);

			double currentMatch = getMatch();
			double currentFreqMatch = getfrequencyMatch();
			double currentSrpMatch = getSrpMatch();

			if (currentMatch > max) {
				max = currentMatch;
			}

			if (currentFreqMatch > maxFreq) {
				maxFreq = currentFreqMatch;
			}

			if (currentSrpMatch > maxSrp) {
				maxSrp = currentSrpMatch;
			}
		}
		match = max;
		frequencyMatch = maxFreq;
		srpMatch = maxSrp;
	}

	public double getMaxMatch() {
		return match;
	}

	public double getMaxFrequnecyMatch() {
		return frequencyMatch;
	}

	public double getMaxSrpMatch() {
		return srpMatch;
	}
}
