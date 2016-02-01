package com.audioant.io.raspberry;

import java.io.BufferedWriter;
import java.io.IOException;

import com.audioant.io.raspberry.hardware.Hardware;

public class AlertController {

	static private BufferedWriter writer;

	public AlertController() throws IOException {
		writer = RaspberryConnection.getWriter();
	}

	public void blink() throws IOException {

		writer.write(Hardware.ALERT_LIGHT.toString());
		writer.flush();
	}
}