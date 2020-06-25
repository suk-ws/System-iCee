package cc.sukazyo.icee.system;

import cc.sukazyo.icee.iCee;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Log {
	
	public static Logger logger = Logger.getLogger(iCee.class.getName());
	
	public static void init () {
		
		PropertyConfigurator.configure(Log.class.getResourceAsStream("/assets/log4j.properties"));
		
	}
	
}
