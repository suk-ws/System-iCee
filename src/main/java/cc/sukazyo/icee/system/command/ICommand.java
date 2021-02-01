package cc.sukazyo.icee.system.command;

import java.util.HashMap;

public interface ICommand {
	
	void execute(String[] args, HashMap<String, String> parameters);
	
}
