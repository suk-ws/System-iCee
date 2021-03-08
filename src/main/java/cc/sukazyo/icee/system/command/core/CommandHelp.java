package cc.sukazyo.icee.system.command.core;

import cc.sukazyo.icee.system.I18n;
import cc.sukazyo.icee.system.Log;
import cc.sukazyo.icee.system.command.*;
import cc.sukazyo.icee.util.Var;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CommandHelp implements ICommand, ICommandHelped {
	
	public static final String NAME = "help";
	
	@Override
	public List<String> getRegistryName () {
		return Collections.singletonList(NAME);
	}
	
	@Override
	public CommandType getType () {
		return CommandType.HELPER_COMMAND;
	}
	
	@Override
	public void execute (String[] args, Map<String, String> parameters) {
		
		StringBuilder helper = new StringBuilder();
		
		if (!parameters.isEmpty()) { // 多余参数判定
			
			try {
				throw new CommandException.ParameterUnsupportedException(parameters);
			} catch (CommandException.ParameterUnsupportedException e) {
				Log.logger.error(e.getMessage());
			}
			return;
			
		} else
			if (args.length == 1) { // 单个命令帮助信息
				
				if (CommandManager.getRegisteredCommandsMap().containsKey(args[0])) {
					ICommand target = CommandManager.getRegisteredCommandsMap().get(args[0]);
					if (target instanceof ICommandHelped) {
						if (
								((ICommandHelped)target).getHelp() != null &&
								!"".equals(((ICommandHelped)target).getHelp())
						) {
							helper.append(I18n.get(
									"core.command.help.command_help_page.page",
									new Var("command", args[0]),
									new Var("command_help_page", ((ICommandHelped)target).getHelp())
							)).append('\n');
						} else {
							Log.logger.info(I18n.get(
									"core.command.help.command_help_page.nan",
									new Var("command", args[0])
							));
							return;
						}
					} else {
						Log.logger.info(I18n.get(
								"core.command.help.command_help_page.unsupported",
								new Var("command", args[0])
						));
						return;
					}
				} else {
					Log.logger.warn(I18n.get(
							"core.command.help.command_help_page.unknown",
							new Var("command", args[0])
					));
					return;
				}
				
			} else
				if (args.length == 0) { // 命令列表
					
					helper.append(I18n.get("core.command.help.command_list.title")).append('\n');
					
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
							else
								if (command.getRegistryName().size() == 1)
									helper.append(command.getRegistryName().get(0));
								else
									helper.append(command.getRegistryName().toString());
							// 输出命令调用规则和命令介绍
							if (command instanceof ICommandHelped) {
								if (
										((ICommandHelped)command).getGrammar() != null &&
										!"".equals(((ICommandHelped)command).getGrammar())
								)
									helper.append(' ').append(((ICommandHelped)command).getGrammar());
								helper.append(" - ");
								if (
										((ICommandHelped)command).getIntroduction() == null ||
										"".equals(((ICommandHelped)command).getIntroduction())
								)
									helper.append("N/A");
								else
									helper.append(((ICommandHelped)command).getIntroduction());
							} else {
								helper.append(" - ").append(I18n.get("core.command.help.unsupported"));
							}
							helper.append('\n');
							// 输出命令的别名
							if (isAliasCommand) {
								helper.append('\t').append(CommandType.getPrefixOfAlias())
										.append(I18n.get("core.command.help.command_list.single.alias_title"));
								((ICommandWithAlias)command).getAvailableAliases()
										.forEach(regNames -> helper.append(regNames).append(", "));
								helper.delete(helper.length() - 2, helper.length()).append(".\n");
							}
						}
					});
					
				} else { // 参数提供过多
					try {
						throw new CommandException.CommandNotFoundException(args[0]);
					} catch (CommandException.CommandNotFoundException e) {
						Log.logger.warn(I18n.get("core.command.help.too_much_argument"));
					}
					return;
				}
		
		Log.logger.info(helper.deleteCharAt(helper.length() - 1));
		
	}
	
	@Override
	public String getGrammar () {
		return "[command]";
	}
	
	@Override
	public String getIntroduction () {
		return I18n.get("core.command.help.introduction");
	}
	
	@Override
	public String getHelp () {
		return I18n.get("core.command.help.help");
	}
	
}
