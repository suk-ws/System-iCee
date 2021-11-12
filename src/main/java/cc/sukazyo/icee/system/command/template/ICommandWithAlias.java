package cc.sukazyo.icee.system.command.template;

import cc.sukazyo.icee.system.command.ICommand;

import javax.annotation.Nonnull;
import java.util.List;

public interface ICommandWithAlias extends ICommand {
	
	@Nonnull
	String getName ();
	String[] getAliases ();
	
	List<String> getAvailableAliases ();
	
}
