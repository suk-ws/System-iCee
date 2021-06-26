package cc.sukazyo.icee.system.module;

import cc.sukazyo.icee.system.Log;

public class AfferentModulesRegister {
	
	private static IModule[] afferentModuleStack = new IModule[]{};
	
	/**
	 * 用于暂存「注入模块」<br/>
	 * <font color="red"><b>你不应该调用此方法</b></font><br/>
	 * <font color="orange"><b>这个方法只能被调用一次，新调用的结果会覆盖旧的结果！！</b></font>
	 * @param modules 模块对象列表
	 */
	public static void put (IModule... modules) {
		if (afferentModuleStack.length != 0) {
			Log.logger.warn("It seems that AfferentModuleRegister#put is called repeatedly!");
			Log.logger.warn(
					"The management would continue to execute, which may cause some error, " +
			        "especially some modules may not loaded normally."
			);
		}
		afferentModuleStack = modules;
	}
	
	/**
	 * 用于注册「注入模块」
	 * <font color="red"><b>你不应该调用此方法</b></font>
	 */
	public static void register () {
		ModuleManager.register(AfferentModulesRegister.class, afferentModuleStack);
	}
	
}
