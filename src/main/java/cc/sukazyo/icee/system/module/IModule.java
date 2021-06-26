package cc.sukazyo.icee.system.module;

public interface IModule {
	
	void initialize();
	
	String getRegistryName();
	
	String getVersion();
	
	String getDisplayVersion();
	
}
