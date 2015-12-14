package io.logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import config.Parameters.Audio.Analysis;

public class EventLogger implements Observer {

	private static Logger logger = Logger.getLogger(EventLogger.class.getName());

	public EventLogger() {
		try {

			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
			String date = format.format(new Date(System.currentTimeMillis()));

			String fileName = "res/log/events/" + date + ".log";

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

		String msg = String.format("%.2f", percent * 100);

		if (percent >= Analysis.MATCH_THRESHOLD) {
			logger.info(msg);
		} else if (percent > Analysis.MATCH_THRESHOLD / 3.) {
			logger.fine(msg);
		} else if (percent > 0) {
			logger.finer(msg);
		} else {
			logger.finest(msg);
		}
	}
}
