package cc.sukazyo.icee.module.i;

import cc.sukazyo.icee.module.RunState;

public interface IBot {
	
	void start();
	
	void stop();
	
	RunState getState();
	
}
