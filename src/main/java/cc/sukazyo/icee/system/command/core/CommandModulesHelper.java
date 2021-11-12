package cc.sukazyo.icee.system.command.core;

import cc.sukazyo.icee.system.I18n;
import cc.sukazyo.icee.system.Log;
import cc.sukazyo.icee.system.command.*;
import cc.sukazyo.icee.system.command.template.AbsCommandSimplest;
import cc.sukazyo.icee.system.command.template.AbsCommandWithChild;
import cc.sukazyo.icee.system.module.ModuleManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandModulesHelper extends AbsCommandWithChild implements ICommandHelped {
	
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
	
	public static class ListModule extends AbsCommandSimplest {
		
		public static final String NAME = "list";
		
		@Nonnull
		@Override
		public String getName () {
			return NAME;
		}
		
		@Override
		public CommandType getType () {
			return CommandType.HELPER_COMMAND;
		}
		
		@Override
		public void execute () {
			Log.logger.info("\n" + ModuleManager.getModulesDevelopmentTable());
		}
		
	}
	
}
