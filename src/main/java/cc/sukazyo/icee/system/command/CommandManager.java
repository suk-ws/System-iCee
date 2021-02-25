package cc.sukazyo.icee.system.command;

import java.util.Map;

public class CommandManager extends CommandContainer {
	
	private static final CommandManager INSTANCE = new CommandManager();
	
	public static void register (ICommand command) throws CommandException.CommandNameConflictException {
		INSTANCE.putCommand(command);
	}
	
	public static Map<String, ICommand> getRegisteredCommandsMap () {
		return INSTANCE.getCommands();
	}
	
	public static void execute(String[] args)
	throws CommandException.ParameterDuplicatedException, CommandException.CommandNotFoundException, CommandException.ParameterValueUnavailableException {
		INSTANCE.run(args);
	}
	
}
