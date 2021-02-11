package cc.sukazyo.icee.system.command;

import java.util.ArrayList;
import java.util.HashMap;

public class CommandManager {
	
	private static final HashMap<String, ICommand> commands = new HashMap<>();
	private static final HashMap<String, String> parameters = new HashMap<>();
	
	public static void register (ICommand executor) throws CommandException.CommandNameExistException {
		for (String name : executor.getRegistryName()) {
			if (commands.containsKey(name)) {
				throw new CommandException.CommandNameExistException(); // TODO Parameter
			} else {
				commands.put(name, executor);
			}
		}
	}
	
	public static HashMap<String, ICommand> getRegisteredCommandsMap () {
		return commands;
	}
	
	public static void execute(String[] args)
	throws CommandException.ParameterDuplicatedException, CommandException.CommandNotFoundException {
		String command = null;
		ArrayList<String> argsNew = new ArrayList<>();
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("-")) {
				if (!parameters.containsKey(args[i])) {
					parameters.put(args[i], args[i+1]);
				} else {
					throw new CommandException.ParameterDuplicatedException(); // TODO Parameter
				}
				i++;
			} else if (command==null) {
				command = args[i];
			} else {
				argsNew.add(args[i]);
			}
		}
		if (commands.containsKey(command)) {
			commands.get(command).execute(argsNew.toArray(args), parameters);
		} else {
			throw new CommandException.CommandNotFoundException(command);
		}
	}
	
}
