package cc.sukazyo.icee;

import cc.sukazyo.icee.module.Modules;
import cc.sukazyo.icee.system.*;
import cc.sukazyo.icee.system.command.Console;
import cc.sukazyo.icee.system.config.ConfigGeneralExceptions;
import cc.sukazyo.icee.system.config.Configure;
import cc.sukazyo.icee.system.module.AfferentModulesRegister;
import cc.sukazyo.icee.system.module.IModule;
import cc.sukazyo.icee.system.module.ModuleManager;
import cc.sukazyo.icee.system.command.CommandException;
import cc.sukazyo.icee.system.command.CommandManager;
import cc.sukazyo.icee.system.command.core.CoreCommands;
import org.apache.logging.log4j.message.FormattedMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class iCee {
	
	public static final String PACKID = "icee";
	public static final String VERSION = "0.3.2-dev";
	public static final int BUILD_VER = 50;
	public static final boolean DEBUG_MODE = true;
	
	/**
	 * iCee 入口方法其二<br>
	 * 注册了传入模块，之后交由第一个入口方法执行
	 * @see cc.sukazyo.icee.iCee#main(String[])
	 * @param args 程序参数
	 * @param afferentMods 传入模块
	 */
	public static void main (String[] args, IModule... afferentMods) {
		
		AfferentModulesRegister.put(afferentMods);
		
		main(args);
		
	}
	
	/**
	 * iCee 入口方法<br>
	 * 判定
	 * @param args 程序参数
	 */
	public static void main (String[] args) {
		
		try {
			
			if (args == null || args.length == 0) {
				// 启动主程序
				initializeAsSystemMode();
			} else {
				// 运行 CLI
				initializeAsCLIMode();
				try {
					CommandManager.execute(args);
				} catch (CommandException e) {
					Log.logger.fatal("The command cannot be executed due to the following reasons:\n" + e.getMessage());
				}
			}
			
		} catch (Exception | Error e) {
			Log.logger.fatal("!!! Unknown error caused iCee to exit !!!", e);
			iCee.exit(14);
		}
		
	}
	
	/**
	 * iCee 安全退出方法
	 * @param status 退出状态码
	 */
	public static void exit(int status) {
		if (status == Integer.MIN_VALUE) Log.logger.info("iCee System exit with debug tag!~");
		else Log.logger.info("iCee System exit with status " + status);
		InstanceManager.releaseLock();
		if (status == Integer.MIN_VALUE)
		System.exit(0);
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
					"Current Run Directory: {}\n" +
					"This Processor PID: {}\n" +
					"Running Instance PID: {}",
					System.getProperty("user.dir"),
					InstanceManager.currentPID(),
					InstanceManager.instancePID()
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
		Log.logger.info("Starting Console Input Listen...");
		Console.start();
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
		try {
			I18n.index();
			Configure.init();
			Log.renderColor();
			I18n.load();
			CoreCommands.registerAll();
			Modules.registerModules();
			AfferentModulesRegister.register();
			Log.logger.info("Loaded System Commons.");
		} catch (I18n.ParseException e) {
			Log.logger.fatal("Error while loading localization data: ", e);
			iCee.exit(15);
		} catch (ConfigGeneralExceptions ee) {
			Log.logger.fatal("Some error occurred while loading system config!");
			ee.forEach((e, info) -> Log.logger.fatal(new FormattedMessage(
					"EXCEPTION[{}]::",
					Configure.getConfigPageSavePath(info)
			), e));
			iCee.exit(16);
		}
	}
	
}
