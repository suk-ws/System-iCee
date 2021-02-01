package cc.sukazyo.icee.system.command;

import cc.sukazyo.icee.system.Log;

import java.util.HashMap;

public class CoreCommands {
	
	public static class CommandHelp implements ICommand {
		
		@Override
		public void execute(String[] args, HashMap<String, String> parameters) {
			Log.logger.info("Help is WIP");
		}
		
	}
	
	public static void registerAll () {
		try {
			CommandManager.register("help", new CommandHelp());
		} catch (CommandException.CommandNameExistException e) {
			Log.logger.fatal(e); // TODO Output
		}
	}
	
}
