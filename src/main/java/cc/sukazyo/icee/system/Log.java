package cc.sukazyo.icee.system;

import cc.sukazyo.icee.iCee;
import org.apache.log4j.*;

public class Log {
	
	public static String PROMPT;
	public static final String INPUTER = "~@ ";
	
	public static Logger logger = Logger.getLogger(iCee.class.getName());
	
	public static void init () {
		
		if (iCee.DEBUG_MODE)
			PROMPT = "[%d{yyyy-MM-dd HH:mm:ss}][%t][%C:%M:%L][%p]%m%n";
		else
			PROMPT = "[%d{yyyy-MM-dd HH:mm:ss}][%t][%p]%m%n";
		
		PropertyConfigurator.configure(Log.class.getResourceAsStream("/assets/log4j.properties"));
		
		closeInput();
		
	}
	
	public static void openInput () {
		StringBuilder p = new StringBuilder();
		for (int i = 0; i < PROMPT.length(); i++) p.append('\b');
		Logger.getRootLogger().getAppender("console").setLayout(new PatternLayout(p.append(PROMPT).append(INPUTER).toString()));
	}
	
	public static void closeInput () {
		Logger.getRootLogger().getAppender("console").setLayout(new PatternLayout(PROMPT));
	}
	
	public static void showInput() {
		System.out.print(INPUTER);
	}
	
}
