package cc.sukazyo.icee.system;

import cc.sukazyo.icee.util.Pair;
import org.apache.logging.log4j.Level;

public class LogExtraColorLevel {
	
	public static class ColorLevelPair extends Pair<String, Level> {
		public ColorLevelPair (String o, Level o2) {
			super(o, o2);
		}
	}
	
	public static ColorLevelPair[] AVAILABLE_COLORS = new ColorLevelPair[]{
			new ColorLevelPair("white", Level.getLevel("WHITE")),
			new ColorLevelPair("cyan", Level.getLevel("CYAN")),
			new ColorLevelPair("magenta", Level.getLevel("MAGENTA")),
			new ColorLevelPair("blue", Level.getLevel("BLUE")),
			new ColorLevelPair("yellow", Level.getLevel("YELLOW")),
			new ColorLevelPair("green", Level.getLevel("GREEN")),
			new ColorLevelPair("red", Level.getLevel("RED")),
			new ColorLevelPair("black", Level.getLevel("BLACK")),
			new ColorLevelPair("bright white", Level.getLevel("B_WHITE")),
			new ColorLevelPair("bright cyan", Level.getLevel("B_CYAN")),
			new ColorLevelPair("bright magenta", Level.getLevel("B_MAGENTA")),
			new ColorLevelPair("bright blue", Level.getLevel("B_BLUE")),
			new ColorLevelPair("bright yellow", Level.getLevel("B_YELLOW")),
			new ColorLevelPair("bright green", Level.getLevel("B_GREEN")),
			new ColorLevelPair("bright red", Level.getLevel("B_RED")),
			new ColorLevelPair("bright black", Level.getLevel("B_BLACK"))
	};
	
}
