package com.audioant.io.eventObserver;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import com.audioant.io.raspberry.AlertController;

public class EventLights implements Observer {

	private static final int SLEEP_MILLIS = 3000;

	private long time;

	public EventLights() {
		time = System.currentTimeMillis();
	}

	@Override
	public void update(Observable o, Object arg) {

		if (System.currentTimeMillis() - time >= SLEEP_MILLIS) {

			try {
				AlertController alertController = new AlertController();
				alertController.blink();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
