package cc.sukazyo.icee.discord.event;

import cc.sukazyo.icee.iCee;
import cc.sukazyo.icee.system.Lang;
import cc.sukazyo.icee.util.Log;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Objects;

public class CommandReturn {
	
	protected static String command (String[] comm, MessageReceivedEvent event) {
		String ret;
		
		switch (comm[0]) {
			case "debug" :
				ret = debug(comm, event);
				break;
			case "info":
				ret = info();
				break;
			default:
				Log.logger.debug("Unknown command : " + comm[0]);
				ret = Lang.get("command.unknown").replaceAll("\\{\\{command}}", comm[0]);
		}
		return ret;
	}
	
	private static String debug (String[] comm, MessageReceivedEvent event) {
		String ret;
		switch (comm[1]) {
			case "user":
				ret = Lang.get("command.debug.user");
				ret = ret.replaceAll("\\{\\{avatarId}}", Objects.requireNonNull(event.getAuthor().getAvatarId()));
				ret = ret.replaceAll("\\{\\{avatarIdDefault}}", event.getAuthor().getDefaultAvatarId());
				ret = ret.replaceAll("\\{\\{avatarUrl}}", Objects.requireNonNull(event.getAuthor().getAvatarUrl()));
				ret = ret.replaceAll("\\{\\{username}}", event.getAuthor().getName());
				ret = ret.replaceAll("\\{\\{userid}}", event.getAuthor().getId());
				ret = ret.replaceAll("\\{\\{nick}}", ((event.getGuild().getMember(event.getAuthor())).getNickname() == null ? "Null" : event.getGuild().getMember(event.getAuthor()).getNickname()));
				break;
			case "channel":
				ret = Lang.get("command.debug.channel");
				ret = ret.replaceAll("\\{\\{guildname}}", event.getGuild().getName());
				ret = ret.replaceAll("\\{\\{guildid}}", event.getGuild().getId());
				ret = ret.replaceAll("\\{\\{channelname}}", event.getChannel().getName());
				ret = ret.replaceAll("\\{\\{channelid}}", event.getChannel().getId());
				break;
			case "guild":
				ret = Lang.get("command.debug.guild");
				ret = ret.replaceAll("\\{\\{guildname}}", event.getGuild().getName());
				ret = ret.replaceAll("\\{\\{guildid}}", event.getGuild().getId());
				ret = ret.replaceAll("\\{\\{guilddescription}}", (event.getGuild().getDescription() == null ? "Null" : event.getGuild().getDescription()));
				ret = ret.replaceAll("\\{\\{guildowner}}", Objects.requireNonNull(event.getGuild().getOwner()).getAsMention());
				break;
			default:
				ret = Lang.get("command.unknown.child").replaceAll("\\{\\{command}}", comm[1]);
		}
		return ret;
	}
	
	private static String info () {
		String ret = Lang.get("command.info");
		ret = ret.replaceAll("\\{\\{appid}}", iCee.APPID);
		ret = ret.replaceAll("\\{\\{ver}}", iCee.VERSION);
		ret = ret.replaceAll("\\{\\{buildver}}", String.valueOf(iCee.BUILD_VER));
		return ret;
	}

}
