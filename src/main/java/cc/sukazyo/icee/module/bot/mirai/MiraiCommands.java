package cc.sukazyo.icee.module.bot.mirai;

import cc.sukazyo.icee.system.I18n;
import cc.sukazyo.icee.system.command.CommandWithChildAndAlias;
import cc.sukazyo.icee.system.command.ICommandHelped;

public class MiraiCommands extends CommandWithChildAndAlias implements ICommandHelped {
	
	private static final String NAME = "mirai";
	private static final String[] ALIAS = new String[]{"qq"};
	
	@Override
	public String getName() { return NAME; }
	@Override
	public String[] getAliases() { return ALIAS; }
	
	@Override
	public String getGrammar () {
		return null;
	}

	@Override
	public String getIntroduction() {
		return I18n.get("module.mirai.command.introduction");
	}

	@Override
	public String getHelp () {
		return null;
	}
	
}
