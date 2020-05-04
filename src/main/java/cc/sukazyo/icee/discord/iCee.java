package cc.sukazyo.icee.discord;

import cc.sukazyo.icee.discord.event.CommandReturn;
import cc.sukazyo.icee.discord.system.Proper;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;

public class iCee {
	
	public static JDABuilder builder;
	
	public static void main (String[] args) {
		
		Proper.init();
		
		System.out.println(Proper.TOKEN);
		
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
		
	}
	
}
