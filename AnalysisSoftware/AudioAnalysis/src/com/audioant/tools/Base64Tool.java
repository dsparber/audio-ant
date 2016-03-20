package com.audioant.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

public class Base64Tool {

	public static String encode(File file) throws IOException {

		int size = (int) file.length();
		byte[] bytes = new byte[size];

		FileInputStream fs = new FileInputStream(file);
		fs.read(bytes);
		fs.close();

		return Base64.getEncoder().encodeToString(bytes);
	}
}
