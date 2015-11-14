package wave;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LittleEndianRandomAccessFile extends RandomAccessFile {

	public LittleEndianRandomAccessFile(String name, String mode) throws FileNotFoundException {
		super(name, mode);
	}

	public LittleEndianRandomAccessFile(File file, String mode) throws FileNotFoundException {
		super(file, mode);
	}

	public void write(short val) throws IOException {
		writeByte(val);
		writeByte(val >> 8);
	}

	@Override
	public void write(int val) throws IOException {
		write((short) val);
		write((short) val >> 16);
	}

	public short readTwoBytes() throws IOException {
		byte[] bytes = { readByte(), readByte() };
		return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
	}

	public int readInteger() throws IOException {
		byte[] bytes = { readByte(), readByte(), readByte(), readByte() };
		return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}
}
