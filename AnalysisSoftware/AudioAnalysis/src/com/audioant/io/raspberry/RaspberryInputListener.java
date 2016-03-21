package com.audioant.io.raspberry;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Observable;

public class RaspberryInputListener extends Observable {

	private static RaspberryInputListener inputListener;

	public RaspberryInputListener() throws IOException {

		BufferedReader reader = RaspberryConnection.getReader();

		Thread thread = new Thread(() -> {

			String inputLine;

			try {
				while ((inputLine = reader.readLine()) != null) {
					setChanged();
					notifyObservers(inputLine);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		});
		thread.start();
	}

	public static RaspberryInputListener getInstance() throws IOException {

		if (inputListener == null) {
			inputListener = new RaspberryInputListener();
		}

		return inputListener;
	}
}
