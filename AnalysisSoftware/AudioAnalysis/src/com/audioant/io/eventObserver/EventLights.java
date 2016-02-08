package com.audioant.io.eventObserver;

import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.audioant.audio.model.Sound;
import com.audioant.io.raspberry.AlertController;

/**
 *
 * @author Daniel Sparber
 * @year 2016
 *
 * @version 1.0
 */
public class EventLights implements Observer {

	private static final int SLEEP_MILLIS = 1000;

	private long time;

	public EventLights() {
		time = System.currentTimeMillis();
	}

	@Override
	public void update(Observable o, Object arg) {

		if (arg instanceof List<?> && ((List<?>) arg).get(0) instanceof Sound) {

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

}
