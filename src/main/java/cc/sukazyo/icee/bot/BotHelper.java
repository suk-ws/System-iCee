package cc.sukazyo.icee.bot;

import cc.sukazyo.icee.system.Proper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BotHelper {
	
	public static String isBotCalled (String raw, String text) {
		
		for (String focus : Proper.bot.call.raw) {
			
			Matcher mat = Pattern.compile("^\\s*" + focus +"\\s*([\\s\\S]*)$").matcher(raw);
			
			if (mat.find()) {
				return mat.group(1);
			}
			
		}
		
		for (String focus : Proper.bot.call.text) {
			
			Matcher mat = Pattern.compile("^\\s*" + focus +"\\s*([\\s\\S]*)$").matcher(text);
			
			if (mat.find()) {
				return mat.group(1);
			}
			
		}
		
		return null;
	}
	
}
