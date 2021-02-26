package cc.sukazyo.icee;

import cc.sukazyo.icee.module.Modules;
import cc.sukazyo.icee.system.Conf;
import cc.sukazyo.icee.system.Lang;
import cc.sukazyo.icee.system.ModuleManager;
import cc.sukazyo.icee.system.command.CommandException;
import cc.sukazyo.icee.system.command.CommandManager;
import cc.sukazyo.icee.system.Log;
import cc.sukazyo.icee.system.command.CoreCommands;
import cc.sukazyo.icee.util.ConsoleScanner;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class iCee {
	
	public static final String PACKID = "icee";
	public static final String VERSION = "0.3.1-dev";
	public static final int BUILD_VER = 31;
	public static final boolean DEBUG_MODE = true;
	
	public static ConsoleScanner console;
	
	public static void main (String[] args) {
		
		if (args != null && args.length == 0) {
			// 启动主程序
			initializeAsSystemMode();
		} else {
			// 运行 CLI
			initializeAsCLIMode();
			try {
				CommandManager.execute(args);
			} catch (CommandException.ParameterDuplicatedException | CommandException.CommandNotFoundException | CommandException.ParameterValueUnavailableException e) {
				Log.logger.fatal("The command cannot be executed due to the following reasons:\n" + e.getMessage());
			}
			
		}
		
	}
	
	private static void initializeAsSystemMode () {
		Log.initAsSystemMode();
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
		commonUtilsLoad();
		Modules.registerModules();
		ModuleManager.initializeRegisteredModules();
		Log.logger.info("Starting Console Scanner...");
		iCee.console = new ConsoleScanner();
		console.start();
		Log.logger.info("All Complete!");
	}
	
	private static void initializeAsCLIMode () {
		Log.initAsCLIMode();
		commonUtilsLoad();
		Modules.registerModules();
		Log.logger.info("====================================");
//		Log.logger.info("==                                ==");
		Log.logger.info("==        System iCee CLI         ==");
//		Log.logger.info("==                                ==");
		Log.logger.info("====================================");
	}
	
	private static void commonUtilsLoad () {
		Conf.load();
		Lang.init();
		CoreCommands.registerAll();
		Log.logger.info("Loaded System Commons:(Config, Language and CoreCommands)");
	}
	
}
