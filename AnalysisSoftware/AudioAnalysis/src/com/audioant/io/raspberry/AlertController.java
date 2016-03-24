package com.audioant.io.raspberry;

import java.io.BufferedWriter;
import java.io.IOException;

import com.audioant.config.Config;
import com.audioant.io.raspberry.hardware.Button;
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

	public void playSound(String path) throws IOException {

		StringBuilder builder = new StringBuilder();
		builder.append(Hardware.SOUND.toString());
		builder.append(Config.HW_CONTROLLER_SEP);
		builder.append(path);
		builder.append("\r\n");
		writer.write(builder.toString());
		writer.flush();
	}

	public void writeText(String text) throws IOException {

		StringBuilder builder = new StringBuilder();
		builder.append(Hardware.DISPLAY.toString());
		builder.append(Config.HW_CONTROLLER_SEP);
		builder.append(text);
		builder.append("\r\n");
		writer.write(builder.toString());
		writer.flush();
	}

	public void confirmAlert() throws IOException {

		StringBuilder builder = new StringBuilder();
		builder.append(Hardware.BUTTON.toString());
		builder.append(Config.HW_CONTROLLER_SEP);
		builder.append(Button.BUTTON_CONFIRM.toString());
		builder.append("\r\n");
		writer.write(builder.toString());
		writer.flush();

	}

	public static AlertController getInstance() throws IOException {
		if (alertController == null) {
			alertController = new AlertController();
		}
		return alertController;
	}
}