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

	private long startTime;
	private long analysisTime;
	private int count;

	public AudioStreamAnalyser() throws RserveException, IOException {
		super();
		startTime = System.currentTimeMillis();
		analysisTime = 0;
		count = 0;
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

		Runnable runnable = new AnalysisRunnable((int[]) arg);
		Thread thread = new Thread(runnable);
		thread.start();

		count++;
	}

	public double getAvgSampleReceiveTime() {

		long timeDiff = System.currentTimeMillis() - startTime;

		return (double) timeDiff / count;
	}

	public double getAvgAnalysisTime() {

		return (double) analysisTime / count;
	}

	private class AnalysisRunnable implements Runnable {

		private int[] samples;

		public AnalysisRunnable(int[] samples) {
			this.samples = samples;
		}

		@Override
		public void run() {

			long timeStart = System.currentTimeMillis();

			try {
				addRecentFreq(samples, Audio.SAMPLE_RATE);

				double match = getFreqMatch();

				setChanged();
				notifyObservers(match);

			} catch (REngineException | REXPMismatchException e) {
				e.printStackTrace();
			}

			analysisTime += System.currentTimeMillis() - timeStart;
		}
	}
}
