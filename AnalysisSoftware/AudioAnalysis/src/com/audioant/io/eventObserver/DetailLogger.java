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

import com.audioant.audio.model.MatchResult;
import com.audioant.config.Config;
import com.audioant.io.logging.LogFormatter;

/**
 *
 * @author Daniel Sparber
 * @year 2016
 *
 * @version 1.0
 */
public class DetailLogger implements Observer {

	private static Logger logger = Logger.getLogger(DetailLogger.class.getName());

	public DetailLogger() {

		// // suppress the logging output to the console
		// Logger root = Logger.getLogger("");
		// Handler[] handlers = root.getHandlers();
		// for (Handler handler : handlers) {
		// if (handler instanceof ConsoleHandler) {
		// root.removeHandler(handler);
		// }
		// }

		try {

			String date = Config.DATE_FORMAT_FULL_NO_SPACE.format(new Date(System.currentTimeMillis()));

			String fileName = Config.LOG_FOLDER_DETAIL_PATH + date + Config.LOG_SUFFIX;

			new File(fileName).getParentFile().mkdirs();

			Handler handler = new FileHandler(fileName);

			handler.setFormatter(new LogFormatter());
			handler.setLevel(Level.FINEST);
			logger.addHandler(handler);

			logger.setLevel(Level.FINEST);

		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Observable o, Object arg) {

		if (arg instanceof List<?> && ((List<?>) arg).get(0) instanceof MatchResult) {

			@SuppressWarnings("unchecked")
			List<MatchResult> matches = (List<MatchResult>) arg;

			StringBuilder msg = new StringBuilder();

			msg.append("\n");
			for (MatchResult matchResult : matches) {
				msg.append("\t");
				if (matchResult.isMatch()) {
					msg.append('*');
				}
				if (matchResult.isSureMatch()) {
					msg.append('*');
				}
				msg.append("\t");
				msg.append(matchResult.toString());
				msg.append("\n");
			}
			msg.append("\n\n");

			logger.finest(msg.toString().substring(0, msg.length() - 2));
		}
	}
}
