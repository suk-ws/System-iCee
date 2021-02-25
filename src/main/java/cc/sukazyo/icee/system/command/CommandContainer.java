package cc.sukazyo.icee.system.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommandContainer {
	
	private final HashMap<String, ICommand> commands = new HashMap<>();
	private final HashMap<String, String> parameters = new HashMap<>();
	
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
	
	public void run (String[] args)
	throws CommandException.ParameterDuplicatedException, CommandException.CommandNotFoundException, CommandException.ParameterValueUnavailableException {
		String command = null;
		ArrayList<String> argsNew = new ArrayList<>();
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("-")) {
				if (!parameters.containsKey(args[i])) {
					if (args.length < i+1) {
						parameters.put(args[i], args[i + 1]);
					} else {
						throw new CommandException.ParameterValueUnavailableException(args[i], null);
					}
				} else {
					throw new CommandException.ParameterDuplicatedException(args[i], parameters.get(args[i]), args[i + 1]);
				}
				i++;
			} else if (command == null) {
				command = args[i];
			} else {
				argsNew.add(args[i]);
			}
		}
		if (commands.containsKey(command)) {
			commands.get(command).execute((argsNew.toArray(new String[0])), parameters);
		} else {
			throw new CommandException.CommandNotFoundException(command);
		}
	}
	
}
