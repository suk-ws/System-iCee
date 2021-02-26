package cc.sukazyo.icee.module.bot.discord;

import cc.sukazyo.icee.module.bot.discord.event.TextMessageListener;
import cc.sukazyo.icee.common.RunStatus;
import cc.sukazyo.icee.module.bot.IBot;
import cc.sukazyo.icee.system.Conf;
import cc.sukazyo.icee.system.Log;
import cc.sukazyo.icee.system.Resources;
import com.google.gson.Gson;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageEmbed;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.Objects;

public class DiscordBot implements IBot {
	
	public JDA bot;
	private JDABuilder BUILDER;
	
	public DiscordBot () { }
	
	public void initialize () {
		
		BUILDER = JDABuilder.createDefault(Conf.conf.getString("module.bot.discord.token"));
		
		// WIP Activity Customize
		BUILDER.setActivity(Activity.of(Activity.ActivityType.WATCHING, "Sukazyo debug iCee"));
		
		BUILDER.addEventListeners(new TextMessageListener());
		
		if (Conf.conf.getBoolean("module.bot.discord.apply")) {
			start();
		} else {
			Log.logger.info("Discord Bot doesn't applied");
		}
		
	}
	
	@Override
	public void start() {
		if (getStatus().canStart()) {
			try {
				bot = BUILDER.build();
				Log.logger.info("Discord Bot started.");
			} catch (LoginException e) {
				Log.logger.error("Login failed., please check your token or your network", e);
			}
		} else {
			Log.logger.warn("DiscordBot Bot is running or starting!");
		}
	}
	
	@Override
	public void stop() {
		if (getStatus().canStop()) {
			bot.shutdownNow();
			Log.logger.info("DiscordBot Bot stoped.");
		} else {
			Log.logger.warn("DiscordBot Bot have already stoped!");
		}
		
	}
	
	public void setBotActivity (Activity.ActivityType activityType, String activity) {
		bot.getPresence().setActivity(Activity.of(activityType, activity));
		Log.logger.info("Activity set success!");
	}
	
	public void sendDebug (long channelId) {
		try {
			MessageEmbed msg =
					new EmbedBuilder()
							.setColor(0x7db9de)
							.setTitle("iCee Message")
							.setThumbnail("https://srv.sukazyo.cc/assets/icee-icon.png")
							.setFooter("System iCee | since 2020\nCOPYWRONG © SUKAZYO WORKSHOP 2019 - 2020", "https://srv.sukazyo.cc/assets/icee-icon.png")
							.addField("iCee Hi", Resources.getDataAsString("/debug.txt"), false)
							.addField("iCee Debug", "awodapi ndoa dpanw p", true)
							.addField("iCee Debug", "awo dpanw p", true)
							.addField("iCee Debug", "aw ndoa dp p", true)
							.build();
			Objects.requireNonNull(bot.getTextChannelById(channelId)).sendMessage(msg).queue();
			Objects.requireNonNull(bot.getTextChannelById(channelId)).sendMessage(new Gson().toJson(msg.toData())).queue();
			Log.logger.info("Successed send debug info : " + new Gson().toJson(msg.toData()));
		} catch (IOException e) {
			Log.logger.error("Read Debug Message Failed", e);
		}
	}
	
	@Override
	public RunStatus getStatus() {
		if (bot == null)
			return RunStatus.OFF;
		switch (bot.getStatus()) {
			case INITIALIZING:
			case INITIALIZED:
			case LOGGING_IN:
			case CONNECTING_TO_WEBSOCKET:
			case IDENTIFYING_SESSION:
			case AWAITING_LOGIN_CONFIRMATION:
			case LOADING_SUBSYSTEMS:
				return RunStatus.STARTING;
			case CONNECTED:
				return RunStatus.ON;
			case DISCONNECTED:
				return RunStatus.WAITING;
			case RECONNECT_QUEUED:
			case WAITING_TO_RECONNECT:
			case ATTEMPTING_TO_RECONNECT:
				return RunStatus.RESTARTING;
			case SHUTTING_DOWN:
				return RunStatus.STOPPING;
			case SHUTDOWN:
				return RunStatus.OFF;
			case FAILED_TO_LOGIN:
				break;
		}
		throw new RunStatus.StatusUnknownException("Unsupported Status of JDA.Status:" + bot.getStatus().name());
	}
	
}
