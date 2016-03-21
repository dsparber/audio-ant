package com.audioant.io.raspberry;

import java.io.BufferedWriter;
import java.io.IOException;

import com.audioant.config.Config;
import com.audioant.io.raspberry.hardware.Hardware;

public class DisplayController {

	private BufferedWriter writer;

	private static DisplayController displayController;

	private DisplayController() throws IOException {
		writer = RaspberryConnection.getWriter();
	}

	public void write(String text) throws IOException {

		StringBuilder builder = new StringBuilder();
		builder.append(Hardware.DISPLAY.toString());
		builder.append(Config.HW_CONTROLLER_SEP);
		builder.append(text);
		builder.append("\r\n");
		writer.write(builder.toString());
		writer.flush();
	}

	public static DisplayController getInstance() throws IOException {
		if (displayController == null) {
			displayController = new DisplayController();
		}
		return displayController;
	}
}