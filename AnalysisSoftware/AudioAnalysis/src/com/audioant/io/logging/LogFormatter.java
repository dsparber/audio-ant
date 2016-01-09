package com.audioant.io.logging;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import com.audioant.config.Parameters.DateFormat;

public class LogFormatter extends Formatter {

	@Override
	public String format(LogRecord record) {

		String timestamp = DateFormat.FULL_DATE__MILLIS_SEPERATED.format(new Date(System.currentTimeMillis()));

		return String.format("%s %s: %s\n", timestamp, record.getLevel(), record.getMessage());
	}
}
