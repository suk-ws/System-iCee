package cc.sukazyo.icee.module;

import cc.sukazyo.icee.iCee;
import cc.sukazyo.icee.module.bot.discord.DiscordBot;
import cc.sukazyo.icee.system.Log;
import cc.sukazyo.icee.system.module.ModuleManager;
import cc.sukazyo.icee.system.command.CommandException;

public class Modules {
	
//	public static HttpListener http;
	public static DiscordBot discord;
	
	public static void registerModules () {
		
		try {
			
			discord = new DiscordBot();
//			http = new HttpListener();
			
			ModuleManager.register(Modules.class, discord);
			
		} catch (CommandException.CommandNameConflictException e) {
			Log.logger.fatal("Command Conflict when registering Built-in Module!", e);
			iCee.exit(10);
		}
		
		
		Log.logger.info("Built-in Module Registered");
		
	}
	
}
