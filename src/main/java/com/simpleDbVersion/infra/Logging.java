package com.simpleDbVersion.infra;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Logging {

	private static final Logger logger;

	static {
		logger = Logger.getLogger("com.simpleDbVersion");
		configureLogger();

		info("Loading logging at level " + logger.getLevel());
	}

	public static void info(String message) {
		logger.info(message);
	}

	public static void warning(String message) {
		logger.warning(message);
	}

	private static void configureLogger() {
		logger.setUseParentHandlers(false);

		MyFormatter formatter = new MyFormatter();
		ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(formatter);

		logger.addHandler(handler);
	}

	private static class MyFormatter extends Formatter {

		private static final DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

		public String format(LogRecord record) {
			final StringBuilder builder = new StringBuilder(1000);

			builder.append(formatter.format(new Date(record.getMillis()))).append(" - ");
			builder.append("[").append(record.getLevel()).append("] - ");
			builder.append(formatMessage(record));
			builder.append("\n");

			return builder.toString();
		}

		//		public String getHead(Handler h) {
		//			return super.getHead(h);
		//		}
		//
		//		public String getTail(Handler h) {
		//			return super.getTail(h);
		//		}
	}

}
