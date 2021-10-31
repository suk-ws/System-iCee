package cc.sukazyo.icee.module.bot.discord;

import cc.sukazyo.icee.module.Modules;
import cc.sukazyo.icee.system.I18n;
import cc.sukazyo.icee.system.Log;
import cc.sukazyo.icee.system.command.CommandException;
import cc.sukazyo.icee.system.command.CommandWithChildAndAlias;
import cc.sukazyo.icee.system.command.ICommand;
import cc.sukazyo.icee.system.command.ICommandHelped;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class DiscordCommands extends CommandWithChildAndAlias implements ICommandHelped {
	
	private static class DiscordCommandStop implements ICommand {
		
		@Override
		public List<String> getRegistryName () {
			return Collections.singletonList("stop");
		}
		
		@Override
		public CommandType getType () {
			return CommandType.SYSTEM_COMMAND;
		}
		
		@Override
		public void execute (String[] args) {
			Modules.discord.stop();
		}
		
	}
	
	private static class DiscordCommandStart implements ICommand {
		
		@Override
		public List<String> getRegistryName () {
			return Collections.singletonList("start");
		}
		
		@Override
		public CommandType getType () {
			return CommandType.SYSTEM_COMMAND;
		}
		
		@Override
		public void execute (String[] args) {
			Modules.discord.start();
		}
		
	}
	
	private static class DiscordCommandStatus implements ICommand {
		
		@Override
		public List<String> getRegistryName () {
			return Collections.singletonList("status");
		}
		
		@Override
		public CommandType getType () {
			return CommandType.SYSTEM_COMMAND;
		}
		
		@Override
		public void execute (String[] args) {
			Log.logger.info("Status of mirai qq bot: {}", Modules.discord.getStatus().name());
		}
		
	}
	
	private static final String NAME = "java-discord-api";
	private static final String[] ALIAS = new String[]{"discord", "dc", "jda"};
	
	@Override
	public String getName () {
		return NAME;
	}
	@Override
	public String[] getAliases () {
		return ALIAS;
	}
	
	DiscordCommands () throws CommandException.CommandNameConflictException {
		putCommand(new DiscordCommandStart());
		putCommand(new DiscordCommandStop());
		putCommand(new DiscordCommandStatus());
	}
	
	@Nullable
	@Override
	public String getGrammar () {
		return "<start|stop|status>";
	}
	
	@Nullable
	@Override
	public String getIntroduction () {
		return I18n.getText("core.module.jda.command.introduction");
	}
	
	@Nullable
	@Override
	public String getHelp () {
		return I18n.getText("core.module.jda.command.help");
	}
	
}
