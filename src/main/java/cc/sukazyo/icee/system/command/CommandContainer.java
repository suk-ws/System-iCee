package cc.sukazyo.icee.system.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class CommandContainer {
	
	private final HashMap<String, ICommand> commands = new HashMap<>();
	
	public void putCommand (ICommand command) throws CommandException.CommandNameConflictException {
		for (String name : command.getRegistryName()) {
			if (commands.containsKey(name)) {
				throw new CommandException.CommandNameConflictException(name);
			} else {
				commands.put(name, command);
			}
		}
	}
	
	public Map<String, ICommand> getCommands () {
		return commands;
	}
	
	public void execute (String[] args)
	throws CommandException {
		String command = null; // 待运行的命令
		final ArrayList<String> argsNew = new ArrayList<>();
		for (String arg : args) {
			if (command == null) {
				if (arg.startsWith("-")) {
				throw new CommandException.ParameterUnsupportedException(arg);
				} else command = arg;
			} else {
				argsNew.add(arg);
			}
		}
		if (commands.containsKey(command)) {
			commands.get(command).execute((argsNew.toArray(new String[0])));
		} else {
			throw new CommandException.CommandNotFoundException(command);
		}
	}
	
}
