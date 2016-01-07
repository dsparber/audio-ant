package kapitel2.res;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class AiffAudioFile {

	private RandomAccessFile file;
	private AiffFormatEx aiffFormatEx;

	// Default Values
	public AiffAudioFile(String fileName) throws IOException {
		this(fileName, AiffFormatEx.builder().nChannels((short) 1).numSampleFrames(88200).bitsPerSample((short) 16)
				.sampleRate(44100).build());

	}

	public AiffAudioFile(String fileName, AiffFormatEx aiffFormatEx) throws FileNotFoundException {
		this.file = new RandomAccessFile(fileName + ".aif", "rw");
		this.aiffFormatEx = aiffFormatEx;
	}

	public void clearFile() throws IOException {
		file.setLength(0);
	}

	public void writeHeader() throws IOException {
		file.seek(0);
		byte[] form = { 'F', 'O', 'R', 'M' };
		file.write(form);
		file.writeInt((int) (file.length() - 8));
		byte[] aiff = { 'A', 'I', 'F', 'F' };
		file.write(aiff);
		byte[] comm = { 'C', 'O', 'M', 'M' };
		file.write(comm);
		file.writeInt(18);
		file.writeShort(aiffFormatEx.getnChannels());
		file.writeInt(aiffFormatEx.getNumSampleFrames());
		file.writeShort(aiffFormatEx.getBitsPerSample());
		file.write(aiffFormatEx.getSampleRate());
		byte[] ssnd = { 'S', 'S', 'N', 'D' };
		file.write(ssnd);
		file.writeInt((int) (file.length() - 42));
		file.writeInt(0);
		file.writeInt(aiffFormatEx.getBlockSize());
	}

	public void createAudioData(double amp, double f, double phase, int length) throws IOException {
		clearFile();
		phase = phase * Math.PI / 180;
		for (int i = 0; i < length; i++) {
			short sample = (short) (amp * Math.sin(phase + 2 * Math.PI * f * i / 44100));
			file.writeShort(sample);
		}
		writeHeader();
	}
}
