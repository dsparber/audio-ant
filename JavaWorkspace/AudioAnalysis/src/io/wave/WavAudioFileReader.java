package io.wave;

import java.io.IOException;
import java.util.ArrayList;

import audio.wave.Format;
import audio.wave.WaveFormat;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class WavAudioFileReader {

	private LittleEndianRandomAccessFile file;
	private WaveFormat waveFormat;

	public WavAudioFileReader(String fileName) throws IOException {
		file = new LittleEndianRandomAccessFile(fileName, "r");

		waveFormat = readHeader();
	}

	public void writeHeader() throws IOException {
		file.seek(0);

		byte[] chunk0 = { 'R', 'I', 'F', 'F' };
		file.write(chunk0);

		int len = (int) file.length() - 8;
		file.write(len);
		byte[] chunk1 = { 'W', 'A', 'V', 'E' };
		file.write(chunk1);
		byte[] chunk2 = { 'f', 'm', 't', ' ' };
		file.write(chunk2);

		len = 16;
		file.write(len);
		file.write(waveFormat.getFormatTag());
		file.write(waveFormat.getChannels());
		file.write(waveFormat.getSamplesPerSec());
		file.write(waveFormat.getAvgBytesPerSec());
		file.write(waveFormat.getBlockAlign());
		file.write(waveFormat.getBitsPerSample());

		byte[] chunk3 = { 'd', 'a', 't', 'a' };
		file.write(chunk3);

		len = (int) file.length() - 44;
		file.write(len);
	}

	private WaveFormat readHeader() throws IOException {

		WaveFormat waveFormat = new WaveFormat();
		file.seek(0);

		int searchChunk = 0;
		searchChunk = 'f' << 24 | 'm' << 16 | 't' << 8 | ' ';
		for (int i = 0; i < file.length() - 4; i++) {
			if (file.readInt() == searchChunk) {
				break;
			}
			file.seek(file.getFilePointer() - 3);
		}

		file.readInteger(); // Chuck size
		file.readTwoBytes(); // format code
		short nChannels = file.readTwoBytes(); // channels
		int nSamplesPerSec = file.readInteger(); // samples per sec
		file.readInteger(); // avg bytes per sec
		file.readTwoBytes(); // block align
		short wBitsPerSample = file.readTwoBytes();

		waveFormat.setBytesPerSample(wBitsPerSample / 8);
		waveFormat.setChannels(nChannels);
		waveFormat.setFormat(Format.PCM);
		waveFormat.setSamplesPerSec(nSamplesPerSec);

		return waveFormat;
	}

	public int[] readData() throws IOException {

		ArrayList<Integer> samples = new ArrayList<Integer>();

		int searchChunk = 0;
		searchChunk = 'd' << 24 | 'a' << 16 | 't' << 8 | 'a';
		for (int i = 0; i < file.length() - 4; i++) {
			if (file.readInt() == searchChunk) {
				break;
			}
			file.seek(file.getFilePointer() - 3);
		}

		if (waveFormat.getBitsPerSample() == 16) {
			Short data;
			while (file.getFilePointer() < file.length()) {
				data = file.readTwoBytes();
				samples.add(data.intValue());
			}
		} else if (waveFormat.getBitsPerSample() == 32) {
			int data;
			while ((data = file.readInteger()) != -1) {
				samples.add(data);
			}
		}

		int[] result = new int[samples.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = samples.get(i);
		}
		return result;
	}

	public WaveFormat getWaveFormat() {
		return waveFormat;
	}
}