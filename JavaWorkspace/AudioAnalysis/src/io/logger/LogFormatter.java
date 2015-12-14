package io.logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {

	@Override
	public String format(LogRecord record) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		String timestamp = format.format(new Date(System.currentTimeMillis()));

		return String.format("%s %s: %s\n", timestamp, record.getLevel(), record.getMessage());
	}
}
