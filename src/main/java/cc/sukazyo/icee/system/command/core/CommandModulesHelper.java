package cc.sukazyo.icee.system.command.core;

import cc.sukazyo.icee.system.I18n;
import cc.sukazyo.icee.system.Log;
import cc.sukazyo.icee.system.command.CommandException;
import cc.sukazyo.icee.system.command.CommandWithChild;
import cc.sukazyo.icee.system.command.ICommand;
import cc.sukazyo.icee.system.command.ICommandHelped;
import cc.sukazyo.icee.system.module.ModuleManager;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CommandModulesHelper extends CommandWithChild implements ICommandHelped {
	
	public static final String NAME = "module";
	
	public CommandModulesHelper () throws CommandException.CommandNameConflictException {
		putCommand(new ListModule());
	}
	
	@Override
	public List<String> getRegistryName () {
		return Collections.singletonList(NAME);
	}
	
	@Nullable
	@Override
	public String getGrammar () {
		StringBuilder cl = new StringBuilder("<");
		getCommands().keySet().forEach(i -> cl.append(i).append('|'));
		cl.deleteCharAt(cl.length()-1).append('>');
		return cl.toString();
	}
	
	@Nullable
	@Override
	public String getIntroduction () {
		return I18n.getText("core.command.module.introduction");
	}
	
	@Nullable
	@Override
	public String getHelp () {
		return null;
	}
	
	public static class ListModule implements ICommand {
		
		public static final String NAME = "list";
		
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
			Log.logger.info("\n" + ModuleManager.getModulesDevelopmentTable());
		}
		
	}
	
}
