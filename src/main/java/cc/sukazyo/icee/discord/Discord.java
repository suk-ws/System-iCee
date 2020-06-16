package cc.sukazyo.icee.discord;

import cc.sukazyo.icee.discord.event.TextMessageListener;
import cc.sukazyo.icee.system.Proper;
import cc.sukazyo.icee.system.RunState;
import cc.sukazyo.icee.util.Log;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class Discord {
	
	public JDA bot;
	
	public Discord() {
		init();
	}
	
	private void init () {
		
		JDABuilder builder = new JDABuilder(AccountType.BOT);
		builder.setToken(Proper.TOKEN);
		
		builder.setActivity(Activity.of(Activity.ActivityType.WATCHING, "Sukazyo debug iCee"));
		
		builder.addEventListeners(new TextMessageListener());
		
		for (int i = 0; i < 3 && RunState.discord == RunState.STARTING; i++) {
			try {
				bot = builder.build();
				RunState.discord = RunState.RUNNING;
				return;
			} catch (LoginException e) {
				Log.error("Login times:" + (i + 1) + " failed.");
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				System.err.println("[FATAL]System meet Exception while waiting discord login");
				e.printStackTrace();
				System.exit(1);
			}
		}
		Log.error("Login failed 3 times, please check your TOKEN or your NETWORK!");
	}
	
}
