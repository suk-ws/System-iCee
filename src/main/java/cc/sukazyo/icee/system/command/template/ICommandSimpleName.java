package cc.sukazyo.icee.system.command.template;

import cc.sukazyo.icee.system.command.ICommand;

import javax.annotation.Nonnull;

public interface ICommandSimpleName extends ICommand {
	
	@Nonnull
	String getName();
	
}
