package cc.sukazyo.icee.system;

import cc.sukazyo.icee.iCee;
import cc.sukazyo.icee.util.StdLogAdapter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.layout.PatternMatch;

public class Log {
	
	public static final String CONSOLE_APPENDER_NAME = "Console";
	public static String PROMPT;
	public static String PROMPT_ADAPT;
	
	/** iCee Logger */
	public static final Logger logger = LogManager.getLogger(iCee.class);
	
	public static void initAsSystemMode () {
		
		if (iCee.DEBUG_MODE) {
			PROMPT = "[%d{yyyy-MM-dd HH:mm:ss}][%t][%C#%M::%L][%p]%m%n";
			PROMPT_ADAPT = "[%d{yyyy-MM-dd HH:mm:ss}][%t][%X{class}#%X{method}::%X{line}][%p]%m%n";
		} else {
			PROMPT_ADAPT = PROMPT = "[%d{yyyy-MM-dd HH:mm:ss}][%t][%p]%m%n";
		}
		
		setAppender(iCee.DEBUG_MODE?Level.ALL:Level.INFO, PROMPT, PROMPT_ADAPT);
		commonInit();
		
	}
	
	public static void initAsCLIMode () {
		PROMPT_ADAPT = PROMPT = "%m%n";
		setAppender(iCee.DEBUG_MODE?Level.DEBUG:Level.INFO, PROMPT, PROMPT_ADAPT);
		commonInit();
	}
	
	private static void commonInit () {
		
		// System Output TO Log4j2
		System.setOut(new StdLogAdapter.StdOutLogAdapter(logger, System.out));
		System.setErr(new StdLogAdapter.StdErrLogAdapter(logger, System.err));
		
	}
	
	private static void setAppender(Level level, String pattern, String patternAdapt) {
		final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		final org.apache.logging.log4j.core.config.Configuration config = ctx.getConfiguration();
		final PatternLayout layout = PatternLayout.newBuilder().withPatternSelector(
				org.apache.logging.log4j.core.layout.MarkerPatternSelector.newBuilder()
						.setDefaultPattern(PROMPT)
						.setProperties(new PatternMatch[]{
								PatternMatch.newBuilder().setKey("ADAPT").setPattern(PROMPT_ADAPT).build()
						})
						.build()
		).build();
		final Appender appender =
				ConsoleAppender.newBuilder()
						.setName(Log.CONSOLE_APPENDER_NAME)
						.setLayout(layout)
						.withImmediateFlush(true)
						.build();
		appender.start();
		config.addAppender(appender);
		config.getRootLogger().addAppender(appender, level, null);
		ctx.updateLoggers(config);
	}
	
}
