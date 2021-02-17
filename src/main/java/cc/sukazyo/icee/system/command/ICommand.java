package cc.sukazyo.icee.system.command;

import java.util.HashMap;
import java.util.List;

public interface ICommand {
	
	List<String> getRegistryName ();
	
	CommandType getType();
	
	void execute(String[] args, HashMap<String, String> parameters);
	
	String getGrammar ();
	
	String getIntroduction ();
	
	enum CommandType {
		
		/**
		 * 系统级别的命令
		 * 这种命令会操作（读取或更改）系统中运行的资源
		 */
		SYSTEM_COMMAND,
		
		/**
		 * 辅助级别的命令
		 * 这种命令
		 */
		HELPER_COMMAND;
		
		public char getPrefix () {
			switch (this) {
				case SYSTEM_COMMAND:
					return '@';
				case HELPER_COMMAND:
					return '>';
				default:
					return getPrefixOfUnknown();
			}
		}
		
		public static char getPrefixOfUnknown () {
			return '?';
		}
		
		public static char getPrefixOfAlias () {
			return '*';
		}
		
	}
	
}
