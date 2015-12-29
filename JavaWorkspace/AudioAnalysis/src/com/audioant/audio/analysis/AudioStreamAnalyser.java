package com.audioant.audio.analysis;

import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RserveException;

import com.audioant.audio.model.SoundModel;
import com.audioant.config.Parameters.Audio;
import com.audioant.io.microphone.AudioStreamReader;

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

		long timeStart = System.currentTimeMillis();

		try {
			addSamples((int[]) arg, Audio.SAMPLE_RATE);

			List<SoundModel> matches = getMatches();

			if (!matches.isEmpty()) {
				setChanged();
				notifyObservers(matches);
			}

		} catch (REngineException | REXPMismatchException e) {
			e.printStackTrace();
		}

		analysisTime += System.currentTimeMillis() - timeStart;

		count++;
	}

	public double getAvgSampleReceiveTime() {

		long timeDiff = System.currentTimeMillis() - startTime;

		return (double) timeDiff / count;
	}

	public double getAvgAnalysisTime() {

		return (double) analysisTime / count;
	}

}
