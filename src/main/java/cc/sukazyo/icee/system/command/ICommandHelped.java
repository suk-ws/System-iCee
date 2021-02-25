package cc.sukazyo.icee.system.command;

import javax.annotation.Nullable;

public interface ICommandHelped {

	@Nullable
	String getGrammar ();
	
	@Nullable
	String getIntroduction ();
	
	@Nullable
	String getHelp ();
	
}
