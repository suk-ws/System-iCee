package cc.sukazyo.icee.system;

import cc.sukazyo.icee.iCee;
import cc.sukazyo.icee.module.bot.CommonBotMessage;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Variable {
	
	public static String compile (String msg, CommonBotMessage message) {
		switch (message.type) {
			case DISCORD:
				msg = compile(msg, message.jdaEvent);
				break;
		}
		msg = compile(msg);
		return msg;
	}
	
	public static String compile (String msg, MessageReceivedEvent dcEvent) {
		
		Matcher matcher = Pattern.compile("\\{\\{user-([0-9]*)([-a-z]+)}}").matcher(msg);
		while (matcher.find()) {
		
		}
		
		return msg;
	}
	
	public static String compile (String msg, net.dv8tion.jda.api.entities.User dcUser) {
		msg = msg.replaceAll("\\{\\{user-curr-id}}", dcUser.getId());
		msg = msg.replaceAll("\\{\\{user-curr-avatar-id}}", (dcUser.getAvatarId()==null ? I18n.getText("bot.null") : dcUser.getAvatarId()));
		msg = msg.replaceAll("\\{\\{user-curr-avatar-url}}", (dcUser.getAvatarUrl()==null ?
		                                                      I18n.getText("bot.null") : dcUser.getAvatarUrl()));
		msg = msg.replaceAll("\\{\\{user-curr-name}}", dcUser.getName());
		return msg;
	}
	
	public static String compile (String msg, net.dv8tion.jda.api.entities.Guild dcGuild) {
		msg = msg.replaceAll("\\{\\{guild-curr-id}}", dcGuild.getId());
		msg = msg.replaceAll("\\{\\{guild-curr-icon-id}}", (dcGuild.getIconId()==null ? I18n.getText("bot.null") : dcGuild.getIconId()));
		msg = msg.replaceAll("\\{\\{guild-curr-icon-url}}", (dcGuild.getIconUrl()==null ? I18n.getText("bot.null") : dcGuild.getIconUrl()));
		msg = msg.replaceAll("\\{\\{guild-curr-name}}", dcGuild.getName());
		return msg;
	}
	
	public static String compile (String msg, String[] command, int locate) {
		msg = msg.replaceAll("\\{\\{command}}", command[0]);
		msg = msg.replaceAll("\\{\\{command-option}}", command[locate]);
		return msg;
	}
	
	public static String compile (String msg) {
		msg = msg.replaceAll("\\{\\{packid}}", iCee.PACKID);
		msg = msg.replaceAll("\\{\\{version}}", iCee.VERSION);
		msg = msg.replaceAll("\\{\\{build-ver}}", String.valueOf(iCee.BUILD_VER));
		msg = msg.replaceAll("\\{\\{debug-mode}}", String.valueOf(iCee.DEBUG_MODE));
		return msg;
	}
	
}
