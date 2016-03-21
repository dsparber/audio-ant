package com.audioant.io.raspberry;

import java.io.BufferedWriter;
import java.io.IOException;

import com.audioant.config.Config;
import com.audioant.io.raspberry.hardware.Hardware;

public class SoundController {

	private BufferedWriter writer;

	private static SoundController displayController;

	private SoundController() throws IOException {
		writer = RaspberryConnection.getWriter();
	}

	public void play(String path) throws IOException {

		StringBuilder builder = new StringBuilder();
		builder.append(Hardware.SOUND.toString());
		builder.append(Config.HW_CONTROLLER_SEP);
		builder.append(path);
		builder.append("\r\n");
		writer.write(builder.toString());
		writer.flush();
	}

	public static SoundController getInstance() throws IOException {
		if (displayController == null) {
			displayController = new SoundController();
		}
		return displayController;
	}
}