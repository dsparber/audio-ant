package com.audioant;

/**
 *
 * @author Daniel Sparber
 * @year 2016
 *
 * @version 1.0
 */
public class AudioAnt {

	public static void main(String[] args) {

		if (args.length > 0 && args[0].equals("cmd")) {
			CommandLineTool.start();
		} else {
			RaspberryTool.start();
		}

	}

}
