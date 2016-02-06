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

		String option = args[0];

		if (option.equals("cmd")) {
			CommandLineTool.start();
		} else {
			RaspberryTool.start();
		}

	}

}
