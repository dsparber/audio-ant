package com.audioant.io.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Observable;

public class AndroidInputListener extends Observable {

	public AndroidInputListener(BufferedReader reader) throws IOException {

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
}
