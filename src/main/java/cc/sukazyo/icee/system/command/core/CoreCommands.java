package cc.sukazyo.icee.system.command.core;

import cc.sukazyo.icee.iCee;
import cc.sukazyo.icee.system.Log;
import cc.sukazyo.icee.system.command.*;

public abstract class CoreCommands {
	
	public static void registerAll () {
		try {
			CommandManager.register(new CommandHelp());
			CommandManager.register(new CommandExit());
			CommandManager.register(new CommandModulesHelper());
			CommandManager.register(new CommandLogColorTest());
			CommandManager.register(new CommandGc());
			CommandManager.register(new CommandI18n());
		} catch (CommandException.CommandNameConflictException e) {
			Log.logger.fatal("Command conflict occurred while registering core commands!", e);
			iCee.exit(9);
		}
		Log.logger.debug("Registered core commands.");
	}
	
}
