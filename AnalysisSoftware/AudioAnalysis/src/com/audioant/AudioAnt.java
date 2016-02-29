package com.audioant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;

import com.audioant.config.Config;

/**
 *
 * @author Daniel Sparber
 * @year 2016
 *
 * @version 1.0
 */
public class AudioAnt {

	public static void main(String[] args) throws FileNotFoundException {

		if (args.length > 0 && args[0].equals("cmd")) {
			CommandLineTool.start();
		} else {

			String date = Config.DATE_FORMAT_FULL_NO_SPACE.format(new Date(System.currentTimeMillis()));
			String log = Config.LOG_FOLDER_ERROR_PATH + date + Config.LOG_SUFFIX;

			new File(log).getParentFile().mkdirs();

			System.setErr(new PrintStream(new FileOutputStream(log)));

			RaspberryTool.start();
		}

	}

}
