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
			if (args.length > 0) { // 单个命令帮助信息
				
				if (CommandManager.getRegisteredCommandsMap().containsKey(args[0])) {
					ICommand target = CommandManager.getRegisteredCommandsMap().get(args[0]);
					StringBuilder commandName = new StringBuilder(args[0]);
					for (int i = 1; i < args.length; i++) {
						if (target instanceof CommandWithChild) {
							target = ((CommandWithChild)target).getCommands().get(args[i]);
							commandName.append(' ').append(args[i]);
						} else {
							Log.logger.info(I18n.getText(
									"core.command.help.command_help_page.non_child",
									new Var("parent", commandName.toString()),
									new Var("next", args[i])
							));
						}
					}
					Var commandNameVar = new Var("command", commandName.toString());
					boolean isUnsupported = true;
					if (target instanceof ICommandHelped) {
						isUnsupported = false;
						if (
								((ICommandHelped)target).getHelp() != null &&
								!"".equals(((ICommandHelped)target).getHelp())
						) {
							helper.append(I18n.getText(
									"core.command.help.command_help_page.page",
									commandNameVar,
									new Var("command_help_page", ((ICommandHelped)target).getHelp())
							)).append('\n');
						} else {
							helper.append(I18n.getText(
									"core.command.help.command_help_page.nan",
									commandNameVar
							)).append('\n');
						}
					}
					if (target instanceof CommandWithChild && ((CommandWithChild)target).isHelpShowCommandLists()) {
						isUnsupported = false;
						helper.append(I18n.getText("core.command.help.command_help_page.child_list_title", commandNameVar));
						helper.append('\n');
						((CommandWithChild)target).getCommands().forEach(
								(name ,command) -> listCommandsToBuilder(name, command, helper)
						);
					}
					if (isUnsupported) {
						Log.logger.info(I18n.getText(
								"core.command.help.command_help_page.unsupported",
								commandNameVar
						));
						return;
					}
				} else {
					Log.logger.warn(I18n.getText(
							"core.command.help.command_help_page.unknown",
							new Var("command", args[0])
					));
					return;
				}
				
			} else { // 命令列表
				
				helper.append(I18n.getText("core.command.help.command_list.title")).append('\n');
				
				CommandManager.getRegisteredCommandsMap().forEach((name, command) -> listCommandsToBuilder(name, command, helper));
				
			}
		
		Log.logger.info(helper.deleteCharAt(helper.length() - 1));
		
	}
	
	@Override
	public String getGrammar () {
		return "[command]";
	}
	
	@Override
	public String getIntroduction () {
		return I18n.getText("core.command.help.introduction");
	}
	
	@Override
	public String getHelp () {
		return I18n.getText("core.command.help.help");
	}
	
	private static void listCommandsToBuilder (String name, ICommand command, StringBuilder echo) {
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
			echo.append(command.getType().getPrefix()).append(' '); // 命令名前缀，用于指示命令类型
			if (isAliasCommand)
				echo.append(((ICommandWithAlias)command).getName());
			else
				if (command.getRegistryName().size() == 1)
					echo.append(command.getRegistryName().get(0));
				else
					echo.append(command.getRegistryName().toString());
			// 输出命令调用规则和命令介绍
			if (command instanceof ICommandHelped) {
				if (
						((ICommandHelped)command).getGrammar() != null &&
						!"".equals(((ICommandHelped)command).getGrammar())
				)
					echo.append(' ').append(((ICommandHelped)command).getGrammar());
				echo.append(" - ");
				if (
						((ICommandHelped)command).getIntroduction() == null ||
						"".equals(((ICommandHelped)command).getIntroduction())
				)
					echo.append("N/A");
				else
					echo.append(((ICommandHelped)command).getIntroduction());
			} else {
				echo.append(" - ").append(I18n.getText("core.command.help.unsupported"));
			}
			echo.append('\n');
			// 输出命令的别名
			if (isAliasCommand) {
				echo.append('\t').append(CommandType.getPrefixOfAlias())
						.append(I18n.getText("core.command.help.command_list.single.alias_title"));
				((ICommandWithAlias)command).getAvailableAliases()
						.forEach(regNames -> echo.append(regNames).append(", "));
				echo.delete(echo.length() - 2, echo.length()).append(".\n");
			}
		}
	}
	
}
