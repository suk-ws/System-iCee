package cc.sukazyo.icee.system.command.core;

import cc.sukazyo.icee.system.I18n;
import cc.sukazyo.icee.system.command.template.AbsCommandSimplest;
import cc.sukazyo.icee.system.command.ICommandHelped;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CommandGc extends AbsCommandSimplest implements ICommandHelped {
	
	public static final String NAME = "gc";
	
	@Nonnull
	@Override
	public String getName () {
		return NAME;
	}
	
	@Override
	public CommandType getType () {
		return CommandType.SYSTEM_COMMAND;
	}
	
	@Override
	public void execute () {
		System.gc();
	}
	
	@Nullable
	@Override
	public String getGrammar () {
		return null;
	}
	
	@Nullable
	@Override
	public String getIntroduction () {
		return I18n.getText("core.command.gc.introduction");
	}
	
	@Nullable
	@Override
	public String getHelp () {
		return null;
	}
	
}
