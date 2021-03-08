package cc.sukazyo.icee.system.module;

public class AfferentModulesRegister {
	
	private static IModule[] afferentModuleStack = new IModule[]{};
	
	public static void put (IModule... modules) {
		afferentModuleStack = modules;
	}
	
	public static void register () {
		ModuleManager.register(AfferentModulesRegister.class, afferentModuleStack);
	}
	
}
