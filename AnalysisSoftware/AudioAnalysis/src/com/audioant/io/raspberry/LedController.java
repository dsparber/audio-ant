package com.audioant.io.raspberry;

import java.io.BufferedWriter;
import java.io.IOException;

import com.audioant.config.Parameters.Communication;
import com.audioant.io.raspberry.hardware.Hardware;
import com.audioant.io.raspberry.hardware.Led;

public class LedController {

	static private BufferedWriter writer;

	public LedController() throws IOException {
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
		message.append(Communication.VALUE_SEPERATOR);
		message.append(led);
		message.append(Communication.VALUE_SEPERATOR);
		message.append(option);
		message.append(Communication.VALUE_SEPERATOR);

		writer.write(message.toString());
		writer.flush();
	}
}