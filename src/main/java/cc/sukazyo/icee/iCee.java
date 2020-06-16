package cc.sukazyo.icee;

import cc.sukazyo.icee.discord.Discord;
import cc.sukazyo.icee.mirai.MiraiQQ;
import cc.sukazyo.icee.system.Lang;
import cc.sukazyo.icee.system.Proper;
import cc.sukazyo.icee.util.Log;

public class iCee {
	
	public static final String APPID = "icee-dc";
	public static final String VERSION = "0.1.0-pre1";
	public static final int BUILD_VER = 6;
	public static final boolean DEBUG_MODE = true;
	
	public static Discord discord;
	public static MiraiQQ qq;
	
	public static void main (String[] args) {
		
		Log.info("Starting iCee Discord");
		
		Proper.load();
		Log.init();
		Lang.init();
		
//		discord = new Discord();
		qq = new MiraiQQ();
		
		Log.info("System Done!");
		
	}
	
}
