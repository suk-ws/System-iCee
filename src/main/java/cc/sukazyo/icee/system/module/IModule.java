package cc.sukazyo.icee.system.module;

import cc.sukazyo.icee.common.RunStatus;

public interface IModule {
	
	void initialize();
	
	String getRegistryName();
	
	String getVersion();
	
	String getDisplayVersion();
	
}
