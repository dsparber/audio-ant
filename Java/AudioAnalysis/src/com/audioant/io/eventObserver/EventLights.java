package com.audioant.io.eventObserver;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import com.audioant.io.raspberry.LedController;
import com.audioant.io.raspberry.hardware.Led;

public class EventLights implements Observer {

	@Override
	public void update(Observable o, Object arg) {

		Runnable runnable = () -> {

			try {

				LedController controller = new LedController();
				controller.on(Led.LED_ALERT);
				Thread.sleep(3000);
				controller.off(Led.LED_ALERT);

			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}

		};

		new Thread(runnable).start();
	}

}
