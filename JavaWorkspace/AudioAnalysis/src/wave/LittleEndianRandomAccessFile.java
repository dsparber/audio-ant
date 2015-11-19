package wave;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import tools.LittleEndian;

public class LittleEndianRandomAccessFile extends RandomAccessFile {

	public LittleEndianRandomAccessFile(String name, String mode) throws FileNotFoundException {
		super(name, mode);
	}

	public LittleEndianRandomAccessFile(File file, String mode) throws FileNotFoundException {
		super(file, mode);
	}

	public void write(short val) throws IOException {
		write(LittleEndian.toBytes(val));
	}

	@Override
	public void write(int val) throws IOException {
		write(LittleEndian.toBytes(val));
	}

	public short readTwoBytes() throws IOException {
		byte[] bytes = { readByte(), readByte() };
		return LittleEndian.toShort(bytes);
	}

	public int readInteger() throws IOException {
		byte[] bytes = { readByte(), readByte(), readByte(), readByte() };
		return LittleEndian.toInt(bytes);
	}
}
