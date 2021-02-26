package cc.sukazyo.icee.system;

import java.io.IOException;
import java.util.Properties;

public class Lang {
	
	private static Properties current;
	private static Properties defaulted;
	
	public static void init () {
		try {
			current = new Properties();
			defaulted = new Properties();
//			current.load(new InputStreamReader(
//					Conf.class.getResourceAsStream("/assets/data/lang/icee_" + Conf.conf.getString("system.lang") +".lang"),
//					StandardCharsets.UTF_8));
			current.load(Resources.getAssets("/lang/icee_" + Conf.conf.getString("system.lang") +".lang"));
			defaulted.load(Resources.getAssets("/lang/icee.lang"));
//			defaulted.load(Conf.class.getResourceAsStream("/assets/data/lang/icee.lang"));
		} catch (IOException e) {
			Log.logger.error("Caught an error while reading lang file, does the current lang not exist?", e);
		}
	}
	
	public static String get (String key) {
		return current.getProperty(key, defaulted.getProperty(key, "${"+key+"}"));
	}
	
}
