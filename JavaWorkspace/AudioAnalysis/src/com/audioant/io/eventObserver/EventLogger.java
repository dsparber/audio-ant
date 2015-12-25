package com.audioant.io.eventObserver;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.audioant.config.Parameters.DateFormat;
import com.audioant.config.Parameters.Logging;
import com.audioant.config.Parameters.StringFormatter;
import com.audioant.config.Parameters.Audio.Analysis;
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

		double percent = (double) arg;

		String msg = String.format(StringFormatter.DOUBLE_FORMAT, percent * 100);

		if (percent >= Analysis.STRONGEST_FREQUENCY_MATCH_THRESHOLD) {
			logger.info(msg);
		} else if (percent > Analysis.STRONGEST_FREQUENCY_MATCH_THRESHOLD / 3.) {
			logger.fine(msg);
		} else if (percent > 0) {
			logger.finer(msg);
		} else {
			logger.finest(msg);
		}
	}
}
