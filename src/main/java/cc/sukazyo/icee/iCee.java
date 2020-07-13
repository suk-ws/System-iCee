package cc.sukazyo.icee;

import cc.sukazyo.icee.bot.discord.Discord;
import cc.sukazyo.icee.bot.mirai.MiraiQQ;
import cc.sukazyo.icee.system.HttpListener;
import cc.sukazyo.icee.system.Lang;
import cc.sukazyo.icee.system.Proper;
import cc.sukazyo.icee.util.CScanner;
import cc.sukazyo.icee.system.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class iCee {
	
	public static final String APPID = "icee";
	public static final String VERSION = "0.2.0";
	public static final int BUILD_VER = 14;
	public static final boolean DEBUG_MODE = true;
	
	public static CScanner console;
	public static HttpListener http;
	
	public static Discord discord;
	public static MiraiQQ mirai;
	
	public static void main (String[] args) {
		
		Log.init();
		
		Log.logger.info("==================================================================================================");
		Log.logger.info("                                                                                                  ");
		Log.logger.info("        ___                                                             ___                       ");
		Log.logger.info("      //   ) )                                                        //   ) )                    ");
		Log.logger.info("     ((                  ___    __  ___  ___      _   __         ( ) //         ___      ___      ");
		Log.logger.info("       \\\\     //   / / ((   ) )  / /   //___) ) // ) )  ) )     / / //        //___) ) //___) )   ");
		Log.logger.info("         ) ) ((___/ /   \\ \\     / /   //       // / /  / /     / / //        //       //          ");
		Log.logger.info("  ((___ / /      / / //   ) )  / /   ((____   // / /  / /     / / ((____/ / ((____   ((____       ");
		Log.logger.info("                / /                                                                               ");
		Log.logger.info("          /((  / /                                                     by   Sukazyo Workshop      ");
		Log.logger.info("         //__ / /                                                                                 ");
		Log.logger.info("                                                 Running at " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()) + "                   ");
		Log.logger.info("==================================================================================================");
		
		Log.logger.info("Loading System Module.");
		Proper.load();
		Lang.init();
		console = new CScanner();
		http = new HttpListener();
		Log.logger.info("System Module Load Complete.");
		
		Log.logger.info("Loading Bot Module.");
		discord = new Discord();
		mirai = new MiraiQQ();
		Log.logger.info("Bot Module Load Complete.");
		
		console.start();
		Log.logger.info("Console input opened.");
		Log.logger.info("System Call Done!");
	
	}
	
}
