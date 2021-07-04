package cc.sukazyo.icee.system.command.core;

import cc.sukazyo.icee.system.I18n;
import cc.sukazyo.icee.system.command.ICommand;
import cc.sukazyo.icee.system.command.ICommandHelped;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CommandGc implements ICommand, ICommandHelped {
	
	public static final String NAME = "gc";
	
	@Override
	public List<String> getRegistryName () {
		return Collections.singletonList(NAME);
	}
	
	@Override
	public CommandType getType () {
		return CommandType.SYSTEM_COMMAND;
	}
	
	@Override
	public void execute (String[] args, Map<String, String> parameters) {
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
		return I18n.get("core.command.gc.introduction");
	}
	
	@Nullable
	@Override
	public String getHelp () {
		return null;
	}
	
}
