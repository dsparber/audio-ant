package com.audioant.io.eventObserver;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.audioant.audio.model.Sound;
import com.audioant.config.Config;
import com.audioant.io.logging.LogFormatter;

public class EventLogger implements Observer {

	private static Logger logger = Logger.getLogger(EventLogger.class.getName());

	public EventLogger() {
		try {

			String date = Config.DATE_FORMAT_FULL_NO_SPACE.format(new Date(System.currentTimeMillis()));

			String fileName = Config.LOG_FOLDER_EVENTS_PATH + date + Config.LOG_SUFFIX;

			new File(fileName).getParentFile().mkdirs();

			Handler handler = new FileHandler(fileName);

			handler.setFormatter(new LogFormatter());
			logger.addHandler(handler);

			logger.setLevel(Level.FINE);

		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Observable o, Object arg) {

		@SuppressWarnings("unchecked")
		List<Sound> matches = (List<Sound>) arg;

		StringBuilder msg = new StringBuilder();

		msg.append(String.format("%d match(es): ", matches.size()));

		for (Sound soundModel : matches) {
			msg.append(soundModel.getNumber());
			if (soundModel.isNamed()) {
				msg.append(" (");
				msg.append(soundModel.getName());
				msg.append(")");
			}
			msg.append(", ");
		}

		logger.info(msg.toString().substring(0, msg.length() - 2));
	}
}
