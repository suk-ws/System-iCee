package cc.sukazyo.icee.bot.discord;

import cc.sukazyo.icee.bot.discord.event.TextMessageListener;
import cc.sukazyo.icee.system.Conf;
import cc.sukazyo.icee.system.RunState;
import cc.sukazyo.icee.system.Log;
import cc.sukazyo.icee.util.FileHelper;
import com.google.gson.Gson;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageEmbed;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class Discord {
	
	private static int state = RunState.OFF;
	
	public JDA bot;
	JDABuilder builder = new JDABuilder(AccountType.BOT);
	
	public Discord() {
		
		builder.setToken(Conf.conf.getString("module.bot.discord.token"));
		
		builder.setActivity(Activity.of(Activity.ActivityType.WATCHING, "Sukazyo debug iCee"));
		
		builder.addEventListeners(new TextMessageListener());
		
		if (Conf.conf.getBoolean("module.bot.discord.apply")) {
			start();
		} else {
			Log.logger.info("Discord Bot doesn't applied");
		}
		
	}
	
	public void start() {
		
		if (state < 0) {
			state = RunState.STARTING;
			for (int i = 0; i < 3 && state == RunState.STARTING; i++) {
				try {
					bot = builder.build();
					state = RunState.RUNNING;
					Log.logger.info("Discord Bot started.");
					return;
				} catch (LoginException e) {
					Log.logger.error("Login times:" + (i + 1) + " failed.");
				}
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					System.err.println("[FATAL]System meet Exception while waiting discord login");
					e.printStackTrace();
					System.exit(1);
				}
			}
			Log.logger.error("Login failed 3 times, please check your TOKEN or your NETWORK!");
		} else {
			Log.logger.warn("Discord Bot is running or starting!");
		}
	}
	
	public void stop() {
		
		if (state > -1) {
			bot.shutdownNow();
			state = RunState.OFF;
			Log.logger.info("Discord Bot stoped.");
		} else {
			Log.logger.warn("Discord Bot have already stoped!");
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
							.setFooter("System iCee | since 2020\nCOPYFALSE © SUKAZYO WORKSHOP 2019 - 2020", "https://srv.sukazyo.cc/assets/icee-icon.png")
							.addField("iCee Hi", FileHelper.getDataContent("/debug.txt"), false)
							.addField("iCee Debug", "awodapi ndoa dpanw p", true)
							.addField("iCee Debug", "awo dpanw p", true)
							.addField("iCee Debug", "aw ndoa dp p", true)
							.build();
			bot.getTextChannelById(channelId).sendMessage(msg).queue();
			bot.getTextChannelById(channelId).sendMessage(new Gson().toJson(msg.toData())).queue();
			Log.logger.info("Successed send debug info : " + new Gson().toJson(msg.toData()));
		} catch (IOException e) {
			Log.logger.error("Read Debug Message Failed", e);
		}
	}
	
	public int getState() {
		return state;
	}
	
}
