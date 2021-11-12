package cc.sukazyo.icee.system.command;

import cc.sukazyo.icee.system.Log;

import javax.annotation.Nonnull;
import java.util.Map;

public class CommandManager extends CommandContainer {
	
	private static final CommandManager INSTANCE = new CommandManager();
	
	public static void register (@Nonnull ICommand command) throws CommandException.CommandNameConflictException {
		Log.logger.trace(
				"new command {} registered as name {}",
				command.getClass().getName(), command.getRegistryName().toString()
		);
		INSTANCE.putCommand(command);
	}
	
	public static Map<String, ICommand> getRegisteredCommandsMap () {
		return INSTANCE.getCommands();
	}
	
	public static void run (String[] args)
	throws CommandException {
		INSTANCE.execute(args);
	}
	
}
