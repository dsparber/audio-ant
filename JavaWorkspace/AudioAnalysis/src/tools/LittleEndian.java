package tools;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class LittleEndian {

	public static byte[] toBytes(short val) throws IOException {
		byte[] bytes = new byte[2];
		bytes[0] = (byte) val;
		bytes[1] = (byte) (val >> 8);
		return bytes;
	}

	public static byte[] toBytes(int val) throws IOException {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) val;
		bytes[1] = (byte) (val >> 8);
		bytes[2] = (byte) (val >> 16);
		bytes[3] = (byte) (val >> 24);
		return bytes;
	}

	public static short toShort(byte[] bytes) throws IOException {
		return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
	}

	public static int toInt(byte[] bytes) throws IOException {
		return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}

}
