package com.audioant.io.eventObserver;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import com.audioant.io.raspberry.LedController;
import com.audioant.io.raspberry.hardware.Led;

public class EventLights implements Observer {

	private static final int SLEEP_MILLIS = 3000;

	private long time;

	public EventLights() {
		time = System.currentTimeMillis();
	}

	@Override
	public void update(Observable o, Object arg) {

		if (System.currentTimeMillis() - time >= SLEEP_MILLIS) {

			Runnable runnable = () -> {

				try {

					LedController controller = new LedController();
					controller.on(Led.LED_ALERT);
					Thread.sleep(SLEEP_MILLIS);
					controller.off(Led.LED_ALERT);

				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}

			};

			new Thread(runnable).start();
			time = System.currentTimeMillis();
		}
	}

}
