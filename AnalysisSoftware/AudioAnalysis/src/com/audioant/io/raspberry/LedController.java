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
		set(led, true);
	}

	public void off(Led led) throws IOException {
		set(led, false);
	}

	private void set(Led led, boolean on) throws IOException {

		StringBuilder message = new StringBuilder();

		message.append(Hardware.LED);
		message.append(Communication.VALUE_SEPERATOR);
		message.append(led);
		message.append(Communication.VALUE_SEPERATOR);
		message.append(on);

		writer.write(message.toString());
		writer.flush();
	}
}