package cc.sukazyo.icee.system.command.template;

import cc.sukazyo.icee.system.command.CommandException;

public abstract class AbsCommandSimplest extends AbsCommandSimple implements ICommandSimpleExecute {
	
	@Override
	public void execute (String[] args) throws CommandException {
		if (args.length != 0)
			// TODO command exception collections
			throw new CommandException("Too much arguments: require 0"){};
		execute();
	}
	
}
