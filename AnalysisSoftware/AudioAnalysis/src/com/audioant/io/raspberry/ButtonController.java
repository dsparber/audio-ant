package com.audioant.io.raspberry;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import com.audioant.config.Config;
import com.audioant.io.raspberry.hardware.Button;
import com.audioant.io.raspberry.hardware.Hardware;

public class ButtonController extends Observable implements Observer {

	private static ButtonController buttonController;

	private ButtonController() throws IOException {

		RaspberryInputListener.getInstance().addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {

		String data = (String) arg;

		String[] dataParts = data.split(Character.toString(Config.HW_CONTROLLER_SEP));

		if (dataParts[0].equals(Hardware.BUTTON.toString())) {

			Button button = Button.valueOf(dataParts[1]);

			setChanged();
			notifyObservers(button);
		}

	}

	public static ButtonController getInstance() throws IOException {
		if (buttonController == null) {
			buttonController = new ButtonController();
		}
		return buttonController;
	}

}
