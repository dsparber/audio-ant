package kapitel2.res;

import java.io.IOException;

public class Converter {

	public static void aiffToWave() {
		// TODO implement
	}

	public static void waveToAiff(String in, String out) throws IOException {
		WavAudioFile fileIn = new WavAudioFile(in);

		WaveFormatEx waveFormatEx = fileIn.readHeader();

		AiffFormatEx aiffFormatEx = AiffFormatEx.builder().nChannels(waveFormatEx.nChannels)
				.sampleRate(waveFormatEx.nSamplesPerSec).numSampleFrames(waveFormatEx.nAvgBytesPerSec)
				.bitsPerSample(waveFormatEx.wBitsPerSample).build();

		new AiffAudioFile(out, aiffFormatEx);
		// TODO complete methode
	}

	public static void main(String[] args) throws IOException {
		waveToAiff("test.wav", "test.aif");
	}
}
