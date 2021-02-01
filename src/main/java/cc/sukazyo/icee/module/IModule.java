package cc.sukazyo.icee.module;

import cc.sukazyo.icee.common.RunStatus;

public interface IModule {
	
	void initialize();
	
	void start();
	
	void stop();
	
	RunStatus getStatus();
	
}
