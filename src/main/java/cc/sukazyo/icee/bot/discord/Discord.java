package cc.sukazyo.icee.bot.discord;

import cc.sukazyo.icee.bot.discord.event.TextMessageListener;
import cc.sukazyo.icee.system.Proper;
import cc.sukazyo.icee.system.RunState;
import cc.sukazyo.icee.system.Log;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class Discord {
	
	private static int state = RunState.OFF;
	
	public JDA bot;
	JDABuilder builder = new JDABuilder(AccountType.BOT);
	
	public Discord() {
		
		builder.setToken(Proper.system.bot.discord.token);
		
		builder.setActivity(Activity.of(Activity.ActivityType.WATCHING, "Sukazyo debug iCee"));
		
		builder.addEventListeners(new TextMessageListener());
		
		if (Proper.system.bot.discord.apply) {
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
	
	public int getState() {
		return state;
	}
	
}
