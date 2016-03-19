package com.audioant.io.android;

import java.util.Observable;
import java.util.Observer;

public class AndroidLogger implements Observer {

	@Override
	public void update(Observable o, Object arg) {
		System.out.println(arg);
	}

	public static AndroidLogger getInstance() {
		return new AndroidLogger();
	}
}
