package com.audioant.io.eventObserver;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import com.audioant.io.raspberry.LEDS;
import com.audioant.io.raspberry.LedController;

public class EventLights implements Observer {

	@Override
	public void update(Observable o, Object arg) {

		Runnable runnable = () -> {

			try {

				LedController controller = new LedController();
				controller.on(LEDS.ALERT);
				Thread.sleep(3000);
				controller.off(LEDS.ALERT);

			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}

		};

		new Thread(runnable).start();
	}

}
