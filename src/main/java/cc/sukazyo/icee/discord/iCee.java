package cc.sukazyo.icee.discord;

import cc.sukazyo.icee.discord.event.CommandReturn;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;

public class iCee {
	
	public static JDABuilder builder;
	public static final String TOKEN = "TOKENTODO";
	
	public static void main (String[] args) {
		
		builder = new JDABuilder(AccountType.BOT);
		builder.setToken(TOKEN);
		
		builder.addEventListener(new CommandReturn());
		
		try {
			builder.buildAsync();
		} catch (LoginException e) {
			System.err.println("Logining Timed Out.");
		}
		
	}
	
}
