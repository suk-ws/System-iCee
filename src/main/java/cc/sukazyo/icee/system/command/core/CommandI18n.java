package cc.sukazyo.icee.system.command.core;

import cc.sukazyo.icee.system.I18n;
import cc.sukazyo.icee.system.Log;
import cc.sukazyo.icee.system.command.*;
import cc.sukazyo.icee.system.command.template.AbsCommandSimple;
import cc.sukazyo.icee.system.command.template.AbsCommandSimplest;
import cc.sukazyo.icee.system.command.template.AbsCommandWithChildAndAlias;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

public class CommandI18n extends AbsCommandWithChildAndAlias implements ICommandHelped {
	
	public static final String NAME = "i18n";
	public static final String[] ALIAS = {"language", "lang"};
	
	public static final Reload childReload = new Reload();
	public static final Index childIndex = new Index();
	public static final Update childUpdate = new Update();
	
	public CommandI18n () throws CommandException.CommandNameConflictException {
		putCommand(childReload);
		putCommand(childIndex);
		putCommand(childUpdate);
	}
	
	private static class Reload extends AbsCommandSimple {
		
		public static final String NAME = "reload";
		
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
		public void execute (String[] args)
		throws CommandException.ParameterUnsupportedException {
			if (args.length == 0) {
				I18n.forEach(I18n.Localized::load);
				Log.logger.info("All language update done.");
			} else {
				for (String langTag : args) {
					I18n.Localized i = I18n.getLocalized(langTag);
					if (i == null) {
						Log.logger.error("Could not found language {}", langTag);
					} else {
						i.load();
						Log.logger.info("Language {} reload done", langTag);
					}
				}
			}
		}
		
	}
	
	private static class Index extends AbsCommandSimplest {
		
		public static final String NAME = "index";
		
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
			try {
				I18n.index();
			} catch (I18n.ParseException e) {
				Log.logger.error("Failed to index the language tree.", e);
			}
		}
		
	}
	
	private static class Update extends AbsCommandSimple {
		
		public static final String NAME = "update";
		
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
		public void execute (String[] args)
		throws CommandException.ParameterUnsupportedException, CommandException.ArgumentUnavailableException {
			if (args.length == 0) {
				childIndex.execute();
				childReload.execute(args);
			} else {
				throw new CommandException.ArgumentUnavailableException(Arrays.toString(args), CommandException.ArgumentUnavailableException.EXCESSIVE_ARGUMENT);
			}
		}
		
	}
	
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
	
	@Nullable
	@Override
	public String getGrammar () {
		return "<<reload|update> [`langTag`]|index>";
	}
	
	@Nullable
	@Override
	public String getIntroduction () {
		return I18n.getText("core.command.i18n.introduction");
	}
	
	@Nullable
	@Override
	public String getHelp () {
		return I18n.getText("core.command.i18n.help");
	}
	
}
