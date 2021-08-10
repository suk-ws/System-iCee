package cc.sukazyo.icee.system.command.core;

import cc.sukazyo.icee.iCee;
import cc.sukazyo.icee.system.I18n;
import cc.sukazyo.icee.system.command.CommandWithAlias;
import cc.sukazyo.icee.system.command.ICommandHelped;

import java.util.Map;

public class CommandExit extends CommandWithAlias implements ICommandHelped {
	
	public static final String NAME = "exit";
	public static final String[] ALIAS = new String[]{"quit", "stop"};
	
	@Override
	public String getName () {
		return NAME;
	}
	
	@Override
	public String[] getAliases () {
		return ALIAS;
	}
	
	@Override
	public CommandType getType () {
		return CommandType.SYSTEM_COMMAND;
	}
	
	@Override
	public void execute (String[] args, Map<String, String> parameters) {
		iCee.exit(0);
	}
	
	@Override
	public String getGrammar () {
		return null;
	}
	
	@Override
	public String getIntroduction () {
		return I18n.getText("core.command.exit.introduction");
	}
	
	@Override
	public String getHelp () {
		return I18n.getText("core.command.exit.help");
	}
	
}
