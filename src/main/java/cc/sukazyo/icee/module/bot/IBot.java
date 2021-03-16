package cc.sukazyo.icee.module.bot;

import cc.sukazyo.icee.common.RunStatus;
import cc.sukazyo.icee.system.module.IModule;

public interface IBot extends IModule {
	
	void start();
	
	void stop();
	
	RunStatus getStatus();
	
}
