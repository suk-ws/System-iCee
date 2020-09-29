package cc.sukazyo.icee.system;

import cc.sukazyo.icee.iCee;
import org.apache.log4j.*;

public class Log {
	
	public static final String PROMPT = "~@ ";
	
	public static Logger logger = Logger.getLogger(iCee.class.getName());
	
	public static void init () {
		
		PropertyConfigurator.configure(Log.class.getResourceAsStream("/assets/log4j.properties"));
		
	}
	
	public static void openInput () {
		Logger.getRootLogger().getAppender("console").setLayout(new PatternLayout("\r[%d{yyyy-MM-dd HH:mm:ss}][%t][%p]%m%n" + PROMPT));
	}
	
	public static void closeInput () {
		Logger.getRootLogger().getAppender("console").setLayout(new PatternLayout("[%d{yyyy-MM-dd HH:mm:ss}][%t][%p]%m%n"));
	}
	
	public static void printPromot () {
		System.out.print(PROMPT);
	}
	
}
