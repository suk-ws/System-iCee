package cc.sukazyo.icee.system.command.template;

import cc.sukazyo.icee.system.Log;
import cc.sukazyo.icee.system.command.CommandContainer;
import cc.sukazyo.icee.system.command.CommandException;
import cc.sukazyo.icee.system.command.ICommand;

import javax.annotation.Nonnull;

public abstract class AbsCommandWithChild extends CommandContainer implements ICommand {
	
	@Override
	public CommandType getType() {
		return CommandType.MULTIPLE_COMMAND;
	}
	
	@Override
	public void putCommand (@Nonnull ICommand child) throws CommandException.CommandNameConflictException {
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
