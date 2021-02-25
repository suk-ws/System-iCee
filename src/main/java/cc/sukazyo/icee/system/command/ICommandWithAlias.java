package cc.sukazyo.icee.system.command;

import java.util.List;

public interface ICommandWithAlias extends ICommand {
	
	String getName ();
	String[] getAliases ();
	
	List<String> getAvailableAliases ();
	
}
