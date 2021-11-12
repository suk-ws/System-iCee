package cc.sukazyo.icee.system.command.template;

import java.util.Collections;
import java.util.List;

public abstract class AbsCommandSimple implements ICommandSimpleName {
	
	@Override
	public List<String> getRegistryName () {
		return Collections.singletonList(getName());
	}
	
}
