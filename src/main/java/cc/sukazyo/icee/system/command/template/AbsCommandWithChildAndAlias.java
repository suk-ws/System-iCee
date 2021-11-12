package cc.sukazyo.icee.system.command.template;

import cc.sukazyo.icee.system.command.CommandManager;

import java.util.ArrayList;
import java.util.List;

public abstract class AbsCommandWithChildAndAlias extends AbsCommandWithChild implements ICommandWithAlias {
	
	public List<String> getAvailableAliases () {
		List<String> names = new ArrayList<>();
		for (String i : getAliases()) {
			if (
					(!CommandManager.getRegisteredCommandsMap().containsKey(i)) ||
					(
							CommandManager.getRegisteredCommandsMap().containsKey(i) &&
							CommandManager.getRegisteredCommandsMap().get(i) == this
					)
			) names.add(i);
		}
		return names;
	}
	
	@Override
	public List<String> getRegistryName() {
		List<String> names = new ArrayList<>();
		names.add(getName());
		names.addAll(getAvailableAliases());
		return names;
	}
	
}
