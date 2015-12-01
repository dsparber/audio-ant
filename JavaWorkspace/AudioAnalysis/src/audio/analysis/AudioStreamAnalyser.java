package audio.analysis;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RserveException;

import config.Parameters.Audio;
import io.microphone.AudioStreamReader;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class AudioStreamAnalyser extends AudioAnalyser implements Observer {

	public AudioStreamAnalyser() throws RserveException, IOException {
		super();
	}

	public void start() {
		reader = new AudioStreamReader();
		reader.addObserver(this);
		reader.streamWindows();
	}

	public void stop() {
		reader.stop();
	}

	@Override
	public void update(Observable o, Object arg) {

		try {
			int[] samples = (int[]) arg;

			addRecentFreq(samples, Audio.SAMPLE_RATE);

			double match = getFreqMatch();

			setChanged();
			notifyObservers(match);

		} catch (REngineException | REXPMismatchException e) {
			e.printStackTrace();
		}
	}
}
