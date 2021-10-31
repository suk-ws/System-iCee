package cc.sukazyo.icee.system.command;

import cc.sukazyo.icee.system.Log;

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
	public void execute(String[] args) {
		try {
			super.execute(args);
		} catch (CommandException.CommandNotFoundException e) {
			Log.logger.error("Child command <" + e.getCommandName() + "> not found on " + getRegistryName().toString());
		} catch (CommandException e) {
			Log.logger.error("Command execute failed: ", e);
		}
	}
	
	public boolean isHelpShowCommandLists () {
		return true;
	}
	
}
