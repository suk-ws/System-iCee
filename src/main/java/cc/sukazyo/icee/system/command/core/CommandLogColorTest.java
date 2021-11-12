package cc.sukazyo.icee.system.command.core;

import cc.sukazyo.icee.system.I18n;
import cc.sukazyo.icee.system.Log;
import cc.sukazyo.icee.system.LogExtraColorLevel;
import cc.sukazyo.icee.system.command.template.AbsCommandSimplest;
import cc.sukazyo.icee.system.command.ICommandHelped;
import cc.sukazyo.icee.system.config.Configure;
import org.apache.logging.log4j.message.FormattedMessage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CommandLogColorTest extends AbsCommandSimplest implements ICommandHelped {
	
	public static final String NAME = "log-colors";
	
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
		Log.logger.info("Start echo colors log.");
		if ("off".equals(Configure.getString(Configure.CORE_ID, "system.log.color.theme"))) {
			Log.logger.warn("Color preview is not available while system.log.color.theme is OFF!");
			return;
		}
		Log.logger.trace("all {} color available", LogExtraColorLevel.AVAILABLE_COLORS.length);
		for (LogExtraColorLevel.ColorLevelPair pair : LogExtraColorLevel.AVAILABLE_COLORS) {
			Log.logger.trace("current color {} as level {}", pair.b, pair.b.intLevel());
			Log.logger.log(pair.b, new FormattedMessage("hello color ! {}", pair.a));
		}
		Log.logger.info("Colors log echo done!.");
	}
	
	@Nullable
	@Override
	public String getGrammar () {
		return null;
	}
	
	@Nullable
	@Override
	public String getIntroduction () {
		return I18n.getText("core.command.log-colors.introduction");
	}
	
	@Nullable
	@Override
	public String getHelp () {
		return null;
	}
	
}
