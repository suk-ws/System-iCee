package cc.sukazyo.icee.system;

import cc.sukazyo.icee.iCee;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.nio.charset.StandardCharsets;

public class Log {
	
	public static final String CONSOLE_APPENDER_NAME = "Console";
	public static String PROMPT;
	
	public static final Logger logger = LogManager.getLogger(iCee.class);
	
	public static void initAsSystemMode () {
		
		if (iCee.DEBUG_MODE)
			PROMPT = "[%d{yyyy-MM-dd HH:mm:ss}][%t][%C:%M:%L][%p]%m%n";
		else
			PROMPT = "[%d{yyyy-MM-dd HH:mm:ss}][%t][%p]%m%n";
		
		setAppender(iCee.DEBUG_MODE?Level.DEBUG:Level.INFO, PROMPT);
		
	}
	
	public static void initAsCLIMode () {
		PROMPT = "%m%n";
		setAppender(iCee.DEBUG_MODE?Level.DEBUG:Level.INFO, PROMPT);
	}
	
	private static void setAppender(Level level, String pattern) {
		final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		final org.apache.logging.log4j.core.config.Configuration config = ctx.getConfiguration();
		final PatternLayout layout = PatternLayout.newBuilder().withCharset(Resources.CHARSET).withConfiguration(config).withPattern(pattern).build();
		final Appender appender = ConsoleAppender.newBuilder().setName(Log.CONSOLE_APPENDER_NAME).setLayout(layout).withImmediateFlush(true).build();
		appender.start();
		config.addAppender(appender);
		config.getRootLogger().addAppender(appender, level, null);
		ctx.updateLoggers(config);
	}
	
}
