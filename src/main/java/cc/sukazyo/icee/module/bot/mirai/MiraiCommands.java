package cc.sukazyo.icee.module.bot.mirai;

import cc.sukazyo.icee.module.Modules;
import cc.sukazyo.icee.system.I18n;
import cc.sukazyo.icee.system.Log;
import cc.sukazyo.icee.system.command.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MiraiCommands extends CommandWithChildAndAlias implements ICommandHelped {
	
	private static class MiraiCommandStop implements ICommand {
		
		@Override
		public List<String> getRegistryName () {
			return Collections.singletonList("stop");
		}
		
		@Override
		public CommandType getType () {
			return CommandType.SYSTEM_COMMAND;
		}
		
		@Override
		public void execute (String[] args, Map<String, String> parameters) {
			Modules.mirai.stop();
		}
		
	}
	
	private static class MiraiCommandStart implements ICommand {
		
		@Override
		public List<String> getRegistryName () {
			return Collections.singletonList("start");
		}
		
		@Override
		public CommandType getType () {
			return CommandType.SYSTEM_COMMAND;
		}
		
		@Override
		public void execute (String[] args, Map<String, String> parameters) {
			Modules.mirai.start();
		}
		
	}
	
	private static class MiraiCommandStatus implements ICommand {
		
		@Override
		public List<String> getRegistryName () {
			return Collections.singletonList("status");
		}
		
		@Override
		public CommandType getType () {
			return CommandType.SYSTEM_COMMAND;
		}
		
		@Override
		public void execute (String[] args, Map<String, String> parameters) {
			Log.logger.info("Status of mirai qq bot: {}", Modules.mirai.getStatus().name());
		}
		
	}
	
	private static final String NAME = "mirai-qq";
	private static final String[] ALIAS = new String[]{"qq", "mirai"};
	
	@Override
	public String getName() { return NAME; }
	@Override
	public String[] getAliases() { return ALIAS; }
	
	MiraiCommands () throws CommandException.CommandNameConflictException {
		putCommand(new MiraiCommandStart());
		putCommand(new MiraiCommandStop());
		putCommand(new MiraiCommandStatus());
	}
	
	@Override
	public String getGrammar () {
		return "<start|stop|status>";
	}

	@Override
	public String getIntroduction() {
		return I18n.get("core.module.mirai_qq.command.introduction");
	}

	@Override
	public String getHelp () {
		return I18n.get("core.module.mirai_qq.command.help");
	}
	
}
