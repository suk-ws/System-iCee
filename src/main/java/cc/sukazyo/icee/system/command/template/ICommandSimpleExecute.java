package cc.sukazyo.icee.system.command.template;

import cc.sukazyo.icee.system.command.CommandException;
import cc.sukazyo.icee.system.command.ICommand;

public interface ICommandSimpleExecute extends ICommand {
	
	void execute () throws CommandException;
	
}
