package cc.sukazyo.icee.system.command;

import cc.sukazyo.icee.system.Log;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public abstract class CoreCommands implements ICommand {
	
	public static class CommandHelp extends CoreCommands {
		
		@Override
		public Collection<String> getRegistryName () {
			return Arrays.asList((new String[]{"help"}).clone());
		}
		
		@Override
		public void execute(String[] args, HashMap<String, String> parameters) {
			Log.logger.info("Help is WIP");
		}
		
	}
	
	public static void registerAll () {
		try {
			CommandManager.register(new CommandHelp());
		} catch (CommandException.CommandNameConflictException e) {
			Log.logger.fatal("Command conflict occurred while registering core commands!", e);
			System.exit(9);
		}
	}
	
}
