package wave;

/**
 *
 * @author Daniel Sparber
 * @year 2015
 *
 * @version 1.0
 */
public class WaveFormat {
	private Format format;
	private short channels;
	private int samplesPerSec;
	private short bytesPerSample;
	private short cbSize;

	// Setter
	public void setFormat(Format format) {
		this.format = format;
	}

	public void setChannels(int channels) {
		this.channels = (short) channels;
	}

	public void setSamplesPerSec(int samplesPerSec) {
		this.samplesPerSec = samplesPerSec;
	}

	public void setBytesPerSample(int bytesPerSample) {
		this.bytesPerSample = (short) bytesPerSample;
	}

	public void setCbSize(int cbSize) {
		this.cbSize = (short) cbSize;
	}

	// Getter
	public short getFormatTag() {
		switch (format) {
		case PCM:
			return 1;
		default:
			return -1;
		}
	}

	public short getChannels() {
		return channels;
	}

	public int getSamplesPerSec() {
		return samplesPerSec;
	}

	public int getAvgBytesPerSec() {
		return getBlockAlign() * getSamplesPerSec();
	}

	public short getBlockAlign() {
		return (short) (getChannels() * ((getBitsPerSample() + 7) / 8));
	}

	public short getBitsPerSample() {
		return (short) (bytesPerSample * 8);
	}

	public short getCbSize() {
		return cbSize;
	}

}