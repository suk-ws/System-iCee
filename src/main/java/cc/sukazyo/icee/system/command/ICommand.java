package cc.sukazyo.icee.system.command;

import java.util.Collection;
import java.util.HashMap;

public interface ICommand {
	
	Collection<String> getRegistryName ();
	
	void execute(String[] args, HashMap<String, String> parameters);
	
}
