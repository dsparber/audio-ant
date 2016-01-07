package com.audioant.io.raspberry;

import java.io.BufferedWriter;
import java.io.IOException;

import com.audioant.config.Parameters.Communication;
import com.audioant.io.raspberry.hardware.HARDWARE;
import com.audioant.io.raspberry.hardware.LEDS;

public class LedController {

	static private BufferedWriter writer;

	public LedController() throws IOException {
		writer = RaspberryConnection.getWriter();
	}

	public void on(LEDS led) throws IOException {
		set(led, true);
	}

	public void off(LEDS led) throws IOException {
		set(led, false);
	}

	private void set(LEDS led, boolean on) throws IOException {

		StringBuilder message = new StringBuilder();

		message.append(HARDWARE.LED);
		message.append(Communication.VALUE_SEPERATOR);
		message.append(led);
		message.append(Communication.VALUE_SEPERATOR);
		message.append(on);

		System.out.println(message.toString());

		writer.write(message.toString());
		writer.flush();
	}
}