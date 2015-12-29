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

import com.audioant.audio.model.SoundModel;
import com.audioant.config.Parameters.DateFormat;
import com.audioant.config.Parameters.Logging;
import com.audioant.io.logging.LogFormatter;

public class EventLogger implements Observer {

	private static Logger logger = Logger.getLogger(EventLogger.class.getName());

	public EventLogger() {
		try {

			String date = DateFormat.FULL_DATE.format(new Date(System.currentTimeMillis()));

			String fileName = Logging.LOG_FOLDER_EVENTS + date + Logging.LOG_SUFFIX;

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
		List<SoundModel> matches = (List<SoundModel>) arg;

		StringBuilder msg = new StringBuilder();

		msg.append(String.format("%d match(es): ", matches.size()));

		for (SoundModel soundModel : matches) {
			msg.append(soundModel.getSoundName());
			msg.append(", ");
		}

		logger.info(msg.toString().substring(0, msg.length() - 2));
	}
}
