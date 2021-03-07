package cc.sukazyo.icee;

import cc.sukazyo.icee.module.Modules;
import cc.sukazyo.icee.system.Conf;
import cc.sukazyo.icee.system.I18n;
import cc.sukazyo.icee.system.InstanceManager;
import cc.sukazyo.icee.system.module.AfferentModulesRegister;
import cc.sukazyo.icee.system.module.IModule;
import cc.sukazyo.icee.system.module.ModuleManager;
import cc.sukazyo.icee.system.command.CommandException;
import cc.sukazyo.icee.system.command.CommandManager;
import cc.sukazyo.icee.system.Log;
import cc.sukazyo.icee.system.command.CoreCommands;
import cc.sukazyo.icee.util.ConsoleScanner;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class iCee {
	
	public static final String PACKID = "icee";
	public static final String VERSION = "0.3.2-dev";
	public static final int BUILD_VER = 33;
	public static final boolean DEBUG_MODE = true;
	
	public static ConsoleScanner console;
	
	public static void main (String[] args, IModule... afferentMod) {
		
		AfferentModulesRegister.put(afferentMod);
		
		main(args);
		
	}
	
	public static void main (String[] args) {
		
		if (args == null || args.length == 0) {
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
	
	public static void exit(int status) {
		Log.logger.info("iCee System exit with status " + status);
		InstanceManager.releaseLock();
		System.exit(status);
	}
	
	private static void initializeAsSystemMode () {
		Log.initAsSystemMode();
		if (!InstanceManager.lock()) {
			Log.logger.fatal(
					"There is already an instance running on the directory.\n" +
				    "Due to the stability and functionality reasons, iCee doesn't support running multiple instance in one directory.\n" +
				    "If you really want to run multiple instance on single machine,\n" +
				    "at present, you can copy iCee binary file to other directory, and run it.\n" +
					"\n" +
					"Current Run Directory: " + System.getProperty("user.dir") + "\n" +
					"This Processor PID: " + InstanceManager.currentPID() + "\n" +
					"Running Instance PID: " + InstanceManager.instancePID()
		    );
		 	exit(11);
		 }
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
		ModuleManager.initializeRegisteredModules();
		Log.logger.info("Starting Console Scanner...");
		iCee.console = new ConsoleScanner();
		console.start();
		Log.logger.info("All Complete!");
	}
	
	private static void initializeAsCLIMode () {
		Log.initAsCLIMode();
		commonUtilsLoad();
		Log.logger.info("====================================");
//		Log.logger.info("==                                ==");
		Log.logger.info("==        System iCee CLI         ==");
//		Log.logger.info("==                                ==");
		Log.logger.info("====================================");
	}
	
	private static void commonUtilsLoad () {
		Conf.load();
		I18n.init();
		CoreCommands.registerAll();
		Modules.registerModules();
		AfferentModulesRegister.register();
		Log.logger.info("Loaded System Commons:(config, language, core-commands, and build-in&afferent modules)");
	}
	
}
