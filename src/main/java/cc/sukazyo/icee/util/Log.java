package cc.sukazyo.icee.util;

import cc.sukazyo.icee.iCee;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {
	
	public static Logger logger = LoggerFactory.getLogger(iCee.class.getName());
	
	public static void init () {
		
		PropertyConfigurator.configure(Log.class.getResourceAsStream("/assets/log4j.properties"));
		
	}
	
}
