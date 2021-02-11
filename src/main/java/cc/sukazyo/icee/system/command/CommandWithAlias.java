package cc.sukazyo.icee.system.command;

import java.util.ArrayList;
import java.util.Collection;

public abstract class CommandWithAlias implements ICommand {
	
	protected abstract String getName();
	protected abstract String[] getAliases();
	
	@Override
	public Collection<String> getRegistryName() {
		Collection<String> names = new ArrayList<>();
		names.add(getName());
		for (String i : getAliases()) {
			if (
					(!CommandManager.getRegisteredCommandsMap().containsKey(i)) || (
							CommandManager.getRegisteredCommandsMap().containsKey(i) &&
							CommandManager.getRegisteredCommandsMap().get(i) == this
					)
			) names.add(i);
		}
		return names;
	}
	
}
