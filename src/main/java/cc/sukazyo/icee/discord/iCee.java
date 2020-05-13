package cc.sukazyo.icee.discord;

import cc.sukazyo.icee.discord.event.TextMessageListener;
import cc.sukazyo.icee.discord.system.Lang;
import cc.sukazyo.icee.discord.system.Proper;
import cc.sukazyo.icee.discord.system.RunState;
import cc.sukazyo.icee.discord.util.Log;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class iCee {
	
	public static final String APPID = "icee-dc";
	public static final String VERSION = "0.1.0-pre1";
	public static final int BUILD_VER = 6;
	public static final boolean DEBUG_MODE = false;
	
	public static JDA discord;
	
	public static void main (String[] args) {
		
		Log.info("Starting iCee Discord");
		
		Proper.load();
		Log.init();
		Lang.init();
		
		discordBot();
		
		Log.info("System Done!");
		
	}
	
	private static void discordBot () {
		
		JDABuilder builder = new JDABuilder(AccountType.BOT);
		builder.setToken(Proper.TOKEN);
		
		builder.setActivity(Activity.of(Activity.ActivityType.WATCHING, "Sukazyo debug iCee"));
		
		builder.addEventListeners(new TextMessageListener());
		
		for (int i = 0; i < 3 && RunState.discord == RunState.STARTING; i++) {
			try {
				discord = builder.build();
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
