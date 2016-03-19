package com.audioant.io.raspberry;

import java.io.BufferedWriter;
import java.io.IOException;

import com.audioant.config.Config;
import com.audioant.io.raspberry.hardware.Hardware;
import com.audioant.io.raspberry.hardware.Led;

public class LedController {

	private BufferedWriter writer;

	private static LedController ledController;

	private LedController() throws IOException {
		writer = RaspberryConnection.getWriter();
	}

	public void on(Led led) throws IOException {
		set(led, 1);
	}

	public void off(Led led) throws IOException {
		set(led, 0);
	}

	public void blink(Led led, int number) throws IOException {
		set(led, number);
	}

	private synchronized void set(Led led, int option) throws IOException {

		StringBuilder message = new StringBuilder();

		message.append(Hardware.LED);
		message.append(Config.HW_CONTROLLER_SEP);
		message.append(led);
		message.append(Config.HW_CONTROLLER_SEP);
		message.append(option);
		message.append(Config.HW_CONTROLLER_SEP);
		message.append("\r\n");

		writer.write(message.toString());
		writer.flush();
	}

	public static LedController getInstance() throws IOException {
		if (ledController == null) {
			ledController = new LedController();
		}
		return ledController;
	}
}