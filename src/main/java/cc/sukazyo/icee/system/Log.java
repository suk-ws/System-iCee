package cc.sukazyo.icee.system;

import cc.sukazyo.icee.iCee;
import cc.sukazyo.icee.system.config.Configure;
import cc.sukazyo.icee.util.SimpleUtils;
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
	@SuppressWarnings("unused")
	public static final String PROMPT_TIME = "[%d{yyyy-MM-dd HH:mm:ss}]";
	@SuppressWarnings("unused")
	public static final String PROMPT_THREAD = "[%t]";
	@SuppressWarnings("unused")
	public static final String PROMPT_LOGGER = "[%c]";
	@SuppressWarnings("unused")
	public static final String PROMPT_STACKTRACE = "[%C#%M::%L]";
	@SuppressWarnings("unused")
	public static final String PROMPT_STACKTRACE_ADAPT = "[%X{class}#%X{method}::%X{line}]";
	@SuppressWarnings("unused")
	public static final String PROMPT_LEVEL = "[%p]";
	@SuppressWarnings("unused")
	public static final String PROMPT_CONTENT = "%m%n";
	@SuppressWarnings("unused")
	public static final String PATTERN_COLOR_PREFIX = "%highlight{";
	/** <font color=orange><b>使用时请注意format空缺的颜色代码</b><font/> */
	@SuppressWarnings("unused")
	public static final String PATTERN_COLOR_SUFFIX =
			"}{FATAL=%s, ERROR=%s, WARN=%s, INFO=%s, DEBUG=%s, TRACE=%s, WHITE=white, CYAN=cyan, MAGENTA=magenta, BLUE=blue, YELLOW=yellow, GREEN=green, RED=red, BLACK=black, B_WHITE=bright white, B_CYAN=bright cyan, B_MAGENTA=bright magenta, B_BLUE=bright blue, B_YELLOW=bright yellow, B_GREEN=bright green, B_RED=bright red, B_BLACK=bright black}";
	public static String PROMPT_DEF;
	public static String PROMPT_ADAPT_DEF;
	public static String PROMPT;
	public static String PROMPT_ADAPT;
	public static Level REQUIRED_LEVEL;
	
	public static String loggerName;
	
	/** iCee Logger */
	public static final Logger logger = LogManager.getLogger(iCee.class);
	
	public static void initAsSystemMode () {
		
		if (iCee.DEBUG_MODE) {
			PROMPT = PROMPT_DEF =
					PROMPT_TIME + PROMPT_THREAD + PROMPT_STACKTRACE + PROMPT_LEVEL + PROMPT_CONTENT;
			PROMPT_ADAPT = PROMPT_ADAPT_DEF =
					PROMPT_TIME + PROMPT_THREAD + PROMPT_STACKTRACE_ADAPT + PROMPT_LEVEL + PROMPT_CONTENT;
		} else {
			PROMPT_ADAPT = PROMPT = PROMPT_ADAPT_DEF = PROMPT_DEF =
					PROMPT_TIME + PROMPT_THREAD + PROMPT_LEVEL + PROMPT_CONTENT;
		}
		REQUIRED_LEVEL = iCee.DEBUG_MODE?Level.ALL:Level.INFO;
		
		updateAppender();
		commonInit();
		
	}
	
	public static void initAsCLIMode () {
		PROMPT_ADAPT = PROMPT = PROMPT_ADAPT_DEF = PROMPT_DEF = PROMPT_CONTENT;
		REQUIRED_LEVEL = iCee.DEBUG_MODE?Level.DEBUG:Level.INFO;
		updateAppender();
		commonInit();
	}
	
	public static void renderColor () {
		String patternColorSuffixFormatted;
		switch (Configure.getString(Configure.CORE_ID, "system.log.color.theme")) {
			case "light":
				patternColorSuffixFormatted = String.format(
						PATTERN_COLOR_SUFFIX,
						"bright red",
						"red",
						"bright magenta",
						"bright green",
						"cyan",
						"white"
				);
				break;
			case "dark":
				patternColorSuffixFormatted = String.format(
						PATTERN_COLOR_SUFFIX,
						"bright red",
						"red",
						"bright magenta",
						"bright green",
						"cyan",
						"blue"
				);
				break;
			case "custom":
				patternColorSuffixFormatted = String.format(
						PATTERN_COLOR_SUFFIX,
						Configure.getString(Configure.CORE_ID, "system.log.color.FATAL"),
						Configure.getString(Configure.CORE_ID, "system.log.color.ERROR"),
						Configure.getString(Configure.CORE_ID, "system.log.color.WARN"),
						Configure.getString(Configure.CORE_ID, "system.log.color.INFO"),
						Configure.getString(Configure.CORE_ID, "system.log.color.DEBUG"),
						Configure.getString(Configure.CORE_ID, "system.log.color.TRACE")
				);
				break;
			case "off":
				return;
			default:
				patternColorSuffixFormatted = "";
		}
		PROMPT = PATTERN_COLOR_PREFIX + PROMPT_DEF + patternColorSuffixFormatted;
		PROMPT_ADAPT = PATTERN_COLOR_PREFIX + PROMPT_ADAPT_DEF + patternColorSuffixFormatted;
		
		updateAppender();
		Log.logger.info("Color set for console log.");
		
	}
	
	private static void commonInit () {
		
		// System Output TO Log4j2
		System.setOut(new StdLogAdapter.StdOutLogAdapter(logger, System.out));
		System.setErr(new StdLogAdapter.StdErrLogAdapter(logger, System.err));
		
	}
	
	/**
	 * 将 log4j2 的控制台 appender 按照当前的配置档进行重新设定<br/>
	 * <font color=red><b>有内存泄漏问题，请尽量不要调用</b></font>
	 */
	public static synchronized void updateAppender () {
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
		String oldLoggerName = null;
		if (loggerName != null) {
			oldLoggerName = loggerName;
		}
		loggerName = String.format("%s$%s", CONSOLE_APPENDER_NAME, SimpleUtils.randomId());
		final Appender appender =
				ConsoleAppender.newBuilder()
						.setName(loggerName)
						.setLayout(layout)
						.withImmediateFlush(true)
						.build();
		appender.start();
		config.getRootLogger().addAppender(appender, REQUIRED_LEVEL, null);
		if (oldLoggerName != null) {
			config.getRootLogger().getAppenders().get(oldLoggerName).stop();
			config.getRootLogger().removeAppender(oldLoggerName);
			SimpleUtils.randomId();
		}
		ctx.updateLoggers(config);
		SimpleUtils.randomId();
	}
	
}
