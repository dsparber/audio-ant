package kapitel2.res;

public class Tools {
	public static byte[] doubleToLongDouble(double inputValue) {
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
		byte outputValue[] = new byte[10];
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
		return outputValue;
	}
}
