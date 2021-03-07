package cc.sukazyo.icee.system.command;

import cc.sukazyo.icee.iCee;
import cc.sukazyo.icee.system.I18n;
import cc.sukazyo.icee.system.Log;

import java.util.*;

public abstract class CoreCommands implements ICommand, ICommandHelped {
	
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
		public void execute(String[] args, Map<String, String> parameters) {
			
			StringBuilder helper = new StringBuilder();
			
			if (!parameters.isEmpty()) { // 多余参数判定
				
				try {
					throw new CommandException.ParameterUnsupportedException(parameters);
				} catch (CommandException.ParameterUnsupportedException e) {
					Log.logger.error(e.getMessage());
				} return;
				
			} else if (args.length == 1) { // 单个命令帮助信息
				
				if (CommandManager.getRegisteredCommandsMap().containsKey(args[0])) {
					ICommand target = CommandManager.getRegisteredCommandsMap().get(args[0]);
					if (target instanceof ICommandHelped) {
						if (((ICommandHelped)target).getHelp() != null && !"".equals(((ICommandHelped)target).getHelp())) {
							helper.append("Help of command `").append(args[0]).append("`").append('\n')
									.append(((ICommandHelped)target).getHelp()).append('\n')
							;
						} else {
							Log.logger.warn(args[0] + ": No help message provided!");
							return;
						}
					} else {
						Log.logger.error("Command `"+args[0]+"` has no help support!");
						return;
					}
				} else {
					Log.logger.error("Unknown command `"+args[0]+"` !");
					return;
				}
				
			} else if (args.length == 0) { // 命令列表
				
				helper.append("All Available Commands: \n");
				
				CommandManager.getRegisteredCommandsMap().forEach((name, command) -> {
					// 检查是否为带有别名的命令
					boolean isAliasCommand = false;
					boolean isAvailableCommand = true;
					if (command instanceof ICommandWithAlias) {
						if (name.equals(((ICommandWithAlias)command).getName()))
							isAliasCommand = true;
						else
							isAvailableCommand = false;
					}
					if (isAvailableCommand) {
						// 输出命令名
						helper.append(command.getType().getPrefix()).append(' '); // 命令名前缀，用于指示命令类型
						if (isAliasCommand)
							helper.append(((ICommandWithAlias)command).getName());
						else if (command.getRegistryName().size() == 1)
							helper.append(command.getRegistryName().get(0));
						else
							helper.append(command.getRegistryName().toString());
						// 输出命令调用规则和命令介绍
						if (command instanceof ICommandHelped) {
							if (((ICommandHelped)command).getGrammar() != null && !"".equals(((ICommandHelped)command).getGrammar()))
								helper.append(' ').append(((ICommandHelped)command).getGrammar());
							helper.append(" - ");
							if (((ICommandHelped)command).getIntroduction() == null || !"".equals(((ICommandHelped)command).getIntroduction()))
								helper.append("N/A");
							else helper.append(((ICommandHelped)command).getIntroduction());
						} else {
							helper.append(" - ").append(I18n.get("command.help.unsupported"));
						}
						helper.append('\n');
						// 输出命令的别名
						if (isAliasCommand) {
							helper.append('\t').append(CommandType.getPrefixOfAlias()).append(" Also available as: ");
							((ICommandWithAlias)command).getAvailableAliases().forEach(regNames -> helper.append(regNames).append(", "));
							helper.delete(helper.length()-2, helper.length()).append(".\n");
						}
					}
				});
				
			} else { // 参数提供过多
				try {
					throw new CommandException.CommandNotFoundException(args[0]);
				} catch (CommandException.CommandNotFoundException e) {
					Log.logger.error("Too much arguments for command help");
				} return;
			}
			
			Log.logger.info(helper.deleteCharAt(helper.length()-1));
			
		}
		
		@Override
		public String getGrammar() {
			return "[command]";
		}
		
		@Override
		public String getIntroduction () {
			return I18n.get("command.help.introduction");
		}
		
		@Override
		public String getHelp () {
			return null;
		}
		
	}
	
	public static class CommandExit extends CommandWithAlias implements ICommandHelped {
		
		@Override
		public String getName() {
			return "exit";
		}
		
		@Override
		public String[] getAliases() {
			return new String[]{"quit", "stop"};
		}
		
		@Override
		public CommandType getType() {
			return CommandType.SYSTEM_COMMAND;
		}
		
		@Override
		public void execute(String[] args, Map<String, String> parameters) {
			iCee.exit(0);
		}
		
		@Override
		public String getGrammar() {
			return null;
		}
		@Override
		public String getIntroduction() {
			return I18n.get("command.exit.introduction");
		}
		
		@Override
		public String getHelp () {
			return null;
		}
		
	}
	
	public static void registerAll () {
		try {
			CommandManager.register(new CommandHelp());
			CommandManager.register(new CommandExit());
		} catch (CommandException.CommandNameConflictException e) {
			Log.logger.fatal("Command conflict occurred while registering core commands!", e);
			iCee.exit(9);
		}
	}
	
}
