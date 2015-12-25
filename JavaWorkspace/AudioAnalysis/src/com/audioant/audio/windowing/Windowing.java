package com.audioant.audio.windowing;

import java.util.ArrayList;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class Windowing {

	public static int[][] generateHannWindows(final int[] audioSignal, final Integer frameLength, final float overlap) {

		// Calculate multiplier
		double[] w = new double[frameLength];
		for (int n = 0; n < w.length; n++) {

			// https://en.wikipedia.org/wiki/Window_function#Generalized_Hamming_windows
			w[n] = 0.5 * (1 - Math.cos((2 * Math.PI * n) / (frameLength - 1)));
		}

		int[][] windows = createWindows(audioSignal, frameLength, overlap);

		// Apply Hann windowing
		for (int i = 0; i < windows.length; i++) {
			for (int n = 0; n < windows[i].length; n++) {
				windows[i][n] = (int) (windows[i][n] * w[n]);
			}
		}

		return windows;
	}

	public static int[][] createWindows(final int[] audioSignal, final Integer frameLength, final float overlap) {

		// Calculate number of overlapping windows
		int numOverlap = Math.round(frameLength * overlap);

		// Calculate number of frames
		int numFrames = (int) Math.ceil((double) audioSignal.length / (double) (frameLength - numOverlap));

		// append zeros at the end of the signal
		Integer[] paddedAudioSignal = new Integer[(numFrames + 1) * (frameLength - numOverlap)];

		for (int i = 0; i < paddedAudioSignal.length; i++) {
			if (i < audioSignal.length) {
				paddedAudioSignal[i] = audioSignal[i];
			} else {
				paddedAudioSignal[i] = 0;
			}
		}

		// Create windows
		ArrayList<int[]> windowList = new ArrayList<int[]>();

		for (int i = 0; i < numFrames; i++) {

			int[] tmp = new int[frameLength];

			boolean onlyZeros = true;

			for (int j = 0; j < tmp.length; j++) {

				int n = j + i * (frameLength - numOverlap);

				tmp[j] = paddedAudioSignal[n];

				if (paddedAudioSignal[n] != 0) {
					onlyZeros = false;
				}
			}
			if (!onlyZeros) {
				windowList.add(tmp);
			}
		}

		int[][] windows = new int[windowList.size()][windowList.get(0).length];

		for (int i = 0; i < windows.length; i++) {
			windows[i] = windowList.get(i);
		}

		return windows;
	}
}
