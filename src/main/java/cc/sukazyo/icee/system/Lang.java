package cc.sukazyo.icee.system;

import cc.sukazyo.icee.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class Lang {
	
	private static Properties current;
	private static Properties defaulted;
	
	public static void init () {
		try {
			current = new Properties();
			defaulted = new Properties();
			current.load(new InputStreamReader(
					Proper.class.getResourceAsStream("/assets/lang/icee_" + Proper.user.lang +".lang"),
					StandardCharsets.UTF_8));
			defaulted.load(Proper.class.getResourceAsStream("/assets/lang/icee_en_us.lang"));
		} catch (IOException e) {
			Log.logger.error("Caught an error while reading lang file, does the current lang not exist?", e);
		}
	}
	
	public static String get (String key) {
		return current.getProperty(key, defaulted.getProperty(key, key));
	}
	
}
