package com.audioant.io.raspberry;

import java.io.BufferedWriter;
import java.io.IOException;

import com.audioant.io.raspberry.hardware.Hardware;

public class AlertController {

	private BufferedWriter writer;

	private static AlertController alertController;

	private AlertController() throws IOException {
		writer = RaspberryConnection.getWriter();
	}

	public void blink() throws IOException {

		writer.write(Hardware.ALERT_LIGHT.toString() + "\r\n");
		writer.flush();
	}

	public static AlertController getInstance() throws IOException {
		if (alertController == null) {
			alertController = new AlertController();
		}
		return alertController;
	}
}