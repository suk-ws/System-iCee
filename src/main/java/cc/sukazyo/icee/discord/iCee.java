package cc.sukazyo.icee.discord;

import cc.sukazyo.icee.discord.event.CommandReturn;
import cc.sukazyo.icee.discord.system.Proper;
import cc.sukazyo.icee.discord.util.Log;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;

public class iCee {
	
	public static final String APPID = "icee-dc";
	public static final String VERSION = "0.1.0";
	public static final int BUILD_VER = 3;
	public static final boolean DEBUG_MODE = true;
	
	public static JDABuilder builder;
	
	public static void main (String[] args) {
		
		Log.info("Starting iCee Discord");
		
		Proper.load();
		Log.init();
		
		builder = new JDABuilder(AccountType.BOT);
		builder.setToken(Proper.TOKEN);
		
		// TODO RESTORE
//		builder.addEventListener(new CommandReturn());
//
//		try {
//			builder.buildAsync();
//		} catch (LoginException e) {
//			System.err.println("Logining Failed.");
//		}
		
		Log.info("System off");
		
	}
	
}
