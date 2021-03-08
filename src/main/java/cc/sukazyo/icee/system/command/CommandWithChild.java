package cc.sukazyo.icee.system.command;

import cc.sukazyo.icee.system.Log;

import java.util.Map;

public abstract class CommandWithChild extends CommandContainer implements ICommand {
	
	@Override
	public CommandType getType() {
		return CommandType.MULTIPLE_COMMAND;
	}
	
	@Override
	public void putCommand (ICommand child) throws CommandException.CommandNameConflictException {
		super.putCommand(child);
	}
	
	@Override
	public void execute(String[] args, Map<String, String> parameters) {
		try {
			run(args);
		} catch (CommandException.ParameterDuplicatedException | CommandException.ParameterValueUnavailableException e) {
			Log.logger.error(e.getMessage());
		} catch (CommandException.CommandNotFoundException e) {
			Log.logger.error("Child command <" + e.getCommandName() + "> not found on " + getRegistryName().toString());
		}
	}
	
}
