package io.eventObserver;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import config.Parameters.Audio.Analysis;
import io.raspberry.LEDS;
import io.raspberry.LedController;

public class EventLights implements Observer {

	@Override
	public void update(Observable o, Object arg) {

		double percent = (double) arg;

		if (percent >= Analysis.MATCH_THRESHOLD) {
			try {

				LedController controller = new LedController();
				controller.on(LEDS.ALERT);
				Thread.sleep(1000);
				controller.off(LEDS.ALERT);
				controller.close();

			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
