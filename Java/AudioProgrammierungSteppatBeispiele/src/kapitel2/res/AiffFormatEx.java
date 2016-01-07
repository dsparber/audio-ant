package kapitel2.res;

public class AiffFormatEx {

	private short nChannels;
	private int numSampleFrames;
	private short bitsPerSample;
	private byte[] sampleRate = new byte[10];
	private int blockSize;

	public static class Builder {
		private short nChannels;
		private int numSampleFrames;
		private short bitsPerSample;
		private byte[] sampleRate = new byte[10];
		private int blockSize;

		public Builder nChannels(short nChannels) {
			this.nChannels = nChannels;
			return this;
		}

		public Builder numSampleFrames(int numSampleFrames) {
			this.numSampleFrames = numSampleFrames;
			return this;
		}

		public Builder bitsPerSample(short bitsPerSample) {
			this.bitsPerSample = bitsPerSample;
			return this;
		}

		public Builder sampleRate(byte[] sampleRate) {
			this.sampleRate = sampleRate;
			return this;
		}

		public Builder sampleRate(int value) {
			sampleRate = Tools.doubleToLongDouble(value);
			return this;
		}

		public Builder blockSize(int blockSize) {
			this.blockSize = blockSize;
			return this;
		}

		public AiffFormatEx build() {
			return new AiffFormatEx(this);
		}
	}

	private AiffFormatEx(Builder builder) {
		this.nChannels = builder.nChannels;
		this.numSampleFrames = builder.numSampleFrames;
		this.bitsPerSample = builder.bitsPerSample;
		this.sampleRate = builder.sampleRate;
		this.blockSize = builder.blockSize;
	}

	public static Builder builder() {
		return new Builder();
	}

	public short getnChannels() {
		return nChannels;
	}

	public int getNumSampleFrames() {
		return numSampleFrames;
	}

	public short getBitsPerSample() {
		return bitsPerSample;
	}

	public byte[] getSampleRate() {
		return sampleRate;
	}

	public int getBlockSize() {
		return blockSize;
	}
}
