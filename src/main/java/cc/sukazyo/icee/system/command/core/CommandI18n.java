package cc.sukazyo.icee.system.command.core;

import cc.sukazyo.icee.system.I18n;
import cc.sukazyo.icee.system.Log;
import cc.sukazyo.icee.system.command.*;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandI18n extends CommandWithChildAndAlias implements ICommandHelped {
	
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
	
	private static class Reload implements ICommand {
		
		public static final String NAME = "reload";
		
		@Override
		public List<String> getRegistryName () {
			return Collections.singletonList(NAME);
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
	
	private static class Index implements ICommand {
		
		public static final String NAME = "index";
		
		@Override
		public List<String> getRegistryName () {
			return Collections.singletonList(NAME);
		}
		
		@Override
		public CommandType getType () {
			return CommandType.SYSTEM_COMMAND;
		}
		
		@Override
		public void execute (String[] args)
		throws CommandException.ParameterUnsupportedException, CommandException.ArgumentUnavailableException {
			if (args.length == 0) {
				try {
					I18n.index();
				} catch (I18n.ParseException e) {
					Log.logger.error("Failed to index the language tree.", e);
				}
			} else {
				throw new CommandException.ArgumentUnavailableException(Arrays.toString(args), CommandException.ArgumentUnavailableException.EXCESSIVE_ARGUMENT);
			}
		}
		
	}
	
	private static class Update implements ICommand {
		
		public static final String NAME = "update";
		
		@Override
		public List<String> getRegistryName () {
			return Collections.singletonList(NAME);
		}
		
		@Override
		public CommandType getType () {
			return CommandType.SYSTEM_COMMAND;
		}
		
		@Override
		public void execute (String[] args)
		throws CommandException.ParameterUnsupportedException, CommandException.ArgumentUnavailableException {
			if (args.length == 0) {
				childIndex.execute(new String[0]);
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
