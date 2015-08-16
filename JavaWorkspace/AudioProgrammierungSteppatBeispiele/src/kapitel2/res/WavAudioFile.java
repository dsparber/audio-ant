package kapitel2.res;

import java.io.IOException;
import java.io.RandomAccessFile;

public class WavAudioFile {

	private RandomAccessFile file;
	private WaveFormatEx waveFormatEx;

	public WavAudioFile(String fileName) throws IOException {
		createFile(fileName);
		writeHeader();
	}

	public void createFile(String fileName) throws IOException {
		file = new RandomAccessFile(fileName, "rw");
		file.setLength(0);
		int i;
		for (i = 0; i < 44; i++) {
			file.writeByte(0);
		}
		waveFormatEx = new WaveFormatEx();
		waveFormatEx.wFormatTag = 1;
		waveFormatEx.nChannels = 1;
		waveFormatEx.nSamplesPerSec = 44100;
		waveFormatEx.nAvgBytesPerSec = 88200;
		waveFormatEx.nBlockAlign = 2;
		waveFormatEx.wBitsPerSample = 16;
		waveFormatEx.cbSize = 0;
	}

	public void createAudioData(double amp, double f, double phase, int length) throws IOException {
		int i;
		phase = phase * Math.PI / 180;
		short sample;
		for (i = 0; i < length; i++) {
			sample = (short) (amp * Math.sin(phase + 2 * Math.PI * f * i / 44100));
			writeShort(sample);
		}
	}

	private void writeShort(short val) throws IOException {
		file.writeByte(val);
		file.writeByte(val >> 8);
	}

	private void writeInt(int val) throws IOException {
		file.writeByte(val);
		file.writeByte(val >> 8);
		file.writeByte(val >> 16);
		file.writeByte(val >> 24);
	}

	public void writeHeader() throws IOException {
		file.seek(0);
		byte[] chunk0 = { 'R', 'I', 'F', 'F' };
		file.write(chunk0);
		int len = (int) file.length() - 8;
		writeInt(len);
		byte[] chunk1 = { 'W', 'A', 'V', 'E' };
		file.write(chunk1);
		byte[] chunk2 = { 'f', 'm', 't', ' ' };
		file.write(chunk2);
		len = 16; // Laenge der auf den fmt -Chunk folgenden Daten
		writeShort(waveFormatEx.wFormatTag);
		writeShort(waveFormatEx.nChannels);
		writeInt(waveFormatEx.nSamplesPerSec);
		writeInt(waveFormatEx.nAvgBytesPerSec);
		writeShort(waveFormatEx.nBlockAlign);
		writeShort(waveFormatEx.wBitsPerSample);
		byte[] chunk3 = { 'd', 'a', 't', 'a' };
		file.write(chunk3);
		len = (int) file.length() - 44;
		writeInt(len);
	}

	public void DoubleToLongDouble(byte[] outputValue, double inputValue) {
		// Berechnung des Exponenten:
		int exponent = (int) Math.floor(Math.log(inputValue) / Math.log(2));
		// Berechnung der Mantisse:
		double temp = Math.pow(2, 63) * (inputValue - Math.pow(2, exponent)) / Math.pow(2, exponent);
		long fraction = (long) temp;
		long ipart = 0x80000000;
		ipart = ipart << 32;
		fraction = fraction | ipart;
		// Werte in das Datenfeld eintragen:
		int biasedExponent = exponent + 16383;
		outputValue[0] = (byte) (biasedExponent >> 8);
		outputValue[1] = (byte) biasedExponent;
		outputValue[2] = (byte) (fraction >> 56);
		outputValue[3] = (byte) (fraction >> 48);
		outputValue[4] = (byte) (fraction >> 40);
		outputValue[5] = (byte) (fraction >> 32);
		outputValue[6] = (byte) (fraction >> 24);
		outputValue[7] = (byte) (fraction >> 16);
		outputValue[8] = (byte) (fraction >> 8);
		outputValue[9] = (byte) (fraction >> 0);
	}

	public int readHeader(RandomAccessFile file, WaveFormatEx waveFormatEx) throws IOException {
		// Filepointer auf Dateianfang setzen:
		file.seek(0);
		// Suchen des fmt-Chunks:
		int searchChunk = 0;
		searchChunk = 'f' << 24 | 'm' << 16 | 't' << 8 | ' ';
		int i;
		int len = 0;
		for (i = 0; i < file.length() - 4; i++) {
			if (file.readInt() == searchChunk) { // Laenge des fmt-Chunks
				len = file.readInt();
				break;
			}
			file.seek(file.getFilePointer() - 3);
		}
		// Lesen des fmt-Chunks:
		waveFormatEx.wFormatTag = file.readShort();
		waveFormatEx.nChannels = file.readShort();
		waveFormatEx.nSamplesPerSec = file.readInt();
		waveFormatEx.nAvgBytesPerSec = file.readInt();
		waveFormatEx.nBlockAlign = file.readShort();
		waveFormatEx.wBitsPerSample = file.readShort();
		// Filepointer auf Dateianfang setzen:
		file.seek(0);
		// Suchen des data-Chunks:
		searchChunk = 0;
		searchChunk = 'd' << 24 | 'a' << 16 | 't' << 8 | 'a';
		for (i = 0; i < file.length() - 4; i++) {
			if (file.readInt() == searchChunk) {
				// Laenge des data-Chunks:
				len = file.readInt();
				break;
			}
			file.seek(file.getFilePointer() - 3);
		}
		return len;
	}

	public double getAmplificationFactor(int numberOfBytes) throws IOException {
		int i;
		short sample;
		short[] b = { 0, 0 };
		double maximumRange = 32767;
		double maximumSample = 0;
		for (i = 0; i < numberOfBytes / waveFormatEx.nBlockAlign; i++) {
			// Abtastwert lesen:
			b[0] = file.readByte();
			b[1] = file.readByte();
			sample = (short) (0xff & b[0] | ((0xff & b[1]) << 8));
			// Maximalwert neu setzen, wenn Abtastwert > Maximalwert:
			if (Math.abs(sample) > maximumSample) {
				maximumSample = Math.abs(sample);
			}
		}
		// Verstaerkungsfaktor berechnen:
		double factor = maximumRange / maximumSample;
		System.out.println("Maximalwert: " + maximumSample + " Verstaerkungsfaktor: " + factor);
		return factor;
	}

	public void amplifyAudioData(double amplitude, int numberOfBytes) throws IOException {
		int i;
		short sample;
		short[] b = { 0, 0 };
		for (i = 0; i < numberOfBytes / waveFormatEx.nBlockAlign; i++) {
			// Lesen des Abtastwerts:
			b[0] = file.readByte();
			b[1] = file.readByte();
			sample = (short) (0xff & b[0] | ((0xff & b[1]) << 8)); // Verstaerkung:
			sample = (short) (amplitude * sample);
			// Filepointer zuruecksetzen:
			file.seek(file.getFilePointer() - 2);
			// Abtastwert zurueckschreiben:
			file.writeByte(sample);
			file.writeByte(sample >> 8);
		}
	}

	public void mixFiles(RandomAccessFile source1File, RandomAccessFile source2File, RandomAccessFile destFile,
			int numberOfBytes) throws IOException {
		int i;
		short out, in1, in2;
		short[] b = { 0, 0 };
		for (i = 0; i < numberOfBytes / waveFormatEx.nBlockAlign; i++) {
			b[0] = source1File.readByte();
			b[1] = source1File.readByte();
			in1 = (short) (0xff & b[0] | ((0xff & b[1]) << 8));
			b[0] = source2File.readByte();
			b[1] = source2File.readByte();
			in2 = (short) (0xff & b[0] | ((0xff & b[1]) << 8));
			out = (short) (in1 + in2);
			destFile.writeByte(out);
			destFile.writeByte(out >> 8);
		}
	}

}