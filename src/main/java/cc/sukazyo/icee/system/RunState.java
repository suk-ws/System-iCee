package cc.sukazyo.icee.system;

import cc.sukazyo.icee.system.call.State;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;

public class RunState {
	
	public static final int OFF = -1;
	public static final int STARTING = 0;
	public static final int RUNNING = 1;
	
	public static ArrayList<State> eventer = new ArrayList<>();
	
	public static int getState (User user) {
		for (int i = 0; i < eventer.size(); i++) {
			if (eventer.get(i).user.equals(user)) {
				return i;
			}
		}
		return -1;
	}
	
}
