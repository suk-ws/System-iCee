package cc.sukazyo.icee.system.command;

import cc.sukazyo.icee.iCee;
import cc.sukazyo.icee.system.Lang;
import cc.sukazyo.icee.system.Log;

import java.util.*;

public abstract class CoreCommands implements ICommand {
	
	public static class CommandHelp extends CoreCommands {
		
		@Override
		public List<String> getRegistryName () {
			return Collections.singletonList("help");
		}
		
		@Override
		public CommandType getType () {
			return CommandType.HELPER_COMMAND;
		}
		
		@Override
		public void execute(String[] args, HashMap<String, String> parameters) {
			StringBuilder helper = new StringBuilder("All Available Commands: \n");
			CommandManager.getRegisteredCommandsMap().forEach((name, command) -> {
				// 检查是否为带有别名的命令
				boolean isAliasCommand = false;
				boolean isAvailableCommand = true;
				if (command instanceof CommandWithAlias) {
					if (name.equals(((CommandWithAlias) command).getName()))
						isAliasCommand = true;
					else
						isAvailableCommand = false;
				}
				if (isAvailableCommand) {
					// 输出命令名
					helper.append(command.getType().getPrefix()).append(' '); // 命令名前缀，用于指示命令类型
					if (isAliasCommand)
						helper.append(((CommandWithAlias) command).getName());
					else if (command.getRegistryName().size() == 1)
						helper.append(command.getRegistryName().get(0));
					else
						helper.append(command.getRegistryName().toString());
					// 输出命令调用规则和命令介绍
					if (command.getGrammar() != null && !command.getGrammar().equals(""))
						helper.append(' ').append(command.getGrammar());
					helper.append(" - ");
					if (command.getIntroduction() == null || command.getIntroduction().equals(""))
						helper.append("N/A");
					else helper.append(command.getIntroduction());
					helper.append('\n');
					// 输出命令的别名
					if (isAliasCommand) {
						helper.append('\t').append(CommandType.getPrefixOfAlias()).append(" Also available as: ");
						((CommandWithAlias) command).getAvailableAliases().forEach(regNames -> helper.append(regNames).append(", "));
						helper.delete(helper.length()-2, helper.length()).append(".\n");
					}
				}
			});
			Log.logger.info(helper.deleteCharAt(helper.length()-1));
		}
		
		@Override
		public String getGrammar() {
			return "[command]";
		}
		
		@Override
		public String getIntroduction () {
			return Lang.get("command.help.introduction");
		}
		
	}
	
	public static class CommandExit extends CommandWithAlias {
		
		@Override
		protected String getName() {
			return "exit";
		}
		
		@Override
		protected String[] getAliases() {
			return new String[]{"quit", "stop"};
		}
		
		@Override
		public CommandType getType() {
			return CommandType.SYSTEM_COMMAND;
		}
		
		@Override
		public void execute(String[] args, HashMap<String, String> parameters) {
			System.exit(0);
		}
		
		@Override
		public String getGrammar() {
			return null;
		}
		@Override
		public String getIntroduction() {
			return Lang.get("command.exit.introduction");
		}
		
	}
	
	public static void registerAll () {
		try {
			CommandManager.register(new CommandHelp());
			CommandManager.register(new CommandExit());
		} catch (CommandException.CommandNameConflictException e) {
			Log.logger.fatal("Command conflict occurred while registering core commands!", e);
			System.exit(9);
		}
	}
	
}
