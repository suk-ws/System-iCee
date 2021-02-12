package cc.sukazyo.icee.system;

import cc.sukazyo.icee.module.IModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModuleManager {
	
	private static final List<IModule> modules = new ArrayList<>();
	
	public static void register (IModule... params) {
		modules.addAll(Arrays.asList(params));
	}
	
	public static void initializeRegisteredModules () {
		modules.forEach(IModule::initialize);
		Log.logger.info("All registered modules have been initialized");
		StringBuilder moduleList = new StringBuilder();
		modules.forEach(i -> moduleList.append(i.getClass().getName()).append("\n"));
		Log.logger.debug("Module List Output\n" +
				"=====================================================\n" +
				"              Registered Module List\n" +
				"-----------------------------------------------------\n" +
				moduleList.toString() +
				"====================================================="
		);
	}
	
}
