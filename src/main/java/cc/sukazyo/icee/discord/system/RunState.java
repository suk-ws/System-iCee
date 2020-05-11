package cc.sukazyo.icee.discord.system;

import net.dv8tion.jda.core.entities.User;

public class RunState {
	
	public static final int RUNNING = 0;
	public static final int STARTING = 1;
	
	public static int discord = RunState.STARTING;
	public static boolean shutdownRun = false;
	
	public static User eventer;
	
}
