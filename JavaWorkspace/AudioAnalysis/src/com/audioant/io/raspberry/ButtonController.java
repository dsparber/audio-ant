package com.audioant.io.raspberry;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Observable;

public class ButtonController extends Observable {

	public ButtonController() throws IOException {

		BufferedReader reader = RaspberryConnection.getReader();

		Thread thread = new Thread(() -> {

			String inputLine;

			try {
				while ((inputLine = reader.readLine()) != null) {
					// TODO Handle input
					setChanged();
					notifyObservers(inputLine);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		});
		thread.start();
	}

}
