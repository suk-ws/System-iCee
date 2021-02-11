package cc.sukazyo.icee;

import cc.sukazyo.icee.module.bot.discord.DiscordBot;
import cc.sukazyo.icee.module.bot.mirai.MiraiBot;
import cc.sukazyo.icee.module.http.HttpListener;
import cc.sukazyo.icee.system.command.CommandException;
import cc.sukazyo.icee.system.command.CommandManager;
import cc.sukazyo.icee.system.Conf;
import cc.sukazyo.icee.system.Lang;
import cc.sukazyo.icee.system.command.CoreCommands;
import cc.sukazyo.icee.util.CScanner;
import cc.sukazyo.icee.system.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class iCee {
	
	public static final String PACKID = "icee";
	public static final String VERSION = "0.3.0-dev";
	public static final int BUILD_VER = 26;
	public static final boolean DEBUG_MODE = true;
	
	public static CScanner console;
	public static HttpListener http;
	
	public static DiscordBot discord;
	public static MiraiBot mirai;
	
	public static void main (String[] args) {
		
		if (args != null && args.length == 0) {
			// 启动主程序
			Log.initAsMainProgramMode();
			moduleRegister();
			start();
		} else {
			// 运行 CLI
			Log.initAsCLIMode();
			moduleRegister();
			Log.logger.info("====================================");
//			Log.logger.info("==                                ==");
			Log.logger.info("==        System iCee CLI         ==");
//			Log.logger.info("==                                ==");
			Log.logger.info("====================================");
			try {
				CommandManager.execute(args);
			} catch (CommandException.ParameterDuplicatedException e) {
				Log.logger.fatal(e); // TODO OutputMessage
			} catch (CommandException.CommandNotFoundException e) {
				Log.logger.fatal(e.getMessage()); // TODO OutputMessage
			}
		}
		
	}
	
	/**
	 * 启动主程序
	 */
	private static void start () {
		
		// 主程序开始的标志输出
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
		
		// 模块初始化
//		http.initialize(); // TODO ALL REBUILD
		discord.initialize();
		mirai.initialize();
		
		// 监听控制台输入
		console.start();
		Log.logger.info("Console input opened.");
		
		// iCee 主程序开启完毕
		Log.logger.info("System Call Done!");
	
	}
	
	private static void moduleRegister() {
		
		// 系统级别模块注册
		Log.logger.info("Registering System Module.");
		Conf.load();
		Lang.init();
		CoreCommands.registerAll();
		console = new CScanner();
		http = new HttpListener();
		Log.logger.info("System Module Register Complete.");
		
		// Bot 模块注册
		Log.logger.info("Registering Bot Module.");
		discord = new DiscordBot();
		mirai = new MiraiBot();
		Log.logger.info("Bot Module Register Complete.");
		
	}
	
}
