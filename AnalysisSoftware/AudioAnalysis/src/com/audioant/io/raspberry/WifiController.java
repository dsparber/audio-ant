package com.audioant.io.raspberry;

import java.io.BufferedWriter;
import java.io.IOException;

import com.audioant.config.Config;
import com.audioant.io.raspberry.hardware.Hardware;

public class WifiController {

	private BufferedWriter writer;

	private static WifiController wifiController;

	private WifiController() throws IOException {
		writer = RaspberryConnection.getWriter();
	}

	public void addNetwork(String ssid, String psk) throws IOException {

		StringBuilder builder = new StringBuilder();
		builder.append(Hardware.WIFI.toString());
		builder.append(Config.HW_CONTROLLER_SEP);
		builder.append(ssid);
		builder.append(Config.HW_CONTROLLER_SEP);
		builder.append(psk);
		builder.append("\r\n");

		writer.write(builder.toString());
		writer.flush();
	}

	public static WifiController getInstance() throws IOException {
		if (wifiController == null) {
			wifiController = new WifiController();
		}
		return wifiController;
	}
}