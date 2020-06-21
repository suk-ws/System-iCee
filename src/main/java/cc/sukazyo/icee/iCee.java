package cc.sukazyo.icee;

import cc.sukazyo.icee.discord.Discord;
import cc.sukazyo.icee.mirai.MiraiQQ;
import cc.sukazyo.icee.system.Lang;
import cc.sukazyo.icee.system.Proper;
import cc.sukazyo.icee.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class iCee {
	
	public static final String APPID = "icee";
	public static final String VERSION = "0.2.0";
	public static final int BUILD_VER = 9;
	public static final boolean DEBUG_MODE = true;
	
	public static Discord discord;
	public static MiraiQQ qq;
	
	public static void main (String[] args) {
		
		Log.init();
		
		Log.logger.info("================================================");
		Log.logger.info("");
		Log.logger.info("             Starting iCee Discord");
		Log.logger.info("");
		Log.logger.info("                        at " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
		Log.logger.info("================================================");
		
		Proper.load();
		Lang.init();
		discord = new Discord();
		qq = new MiraiQQ();
		
		discord.start();
		qq.start();
		
		Log.logger.info("System Call Done!");
	
	}
	
}
