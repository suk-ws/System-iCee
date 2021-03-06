package cc.sukazyo.icee.system.module;

import cc.sukazyo.icee.common.RunStatus;

public interface IModule {
	
	void initialize();
	
	RunStatus getStatus();
	
	String getRegistryName();
	
	String getVersion();
	
	String getDisplayVersion();
	
}
