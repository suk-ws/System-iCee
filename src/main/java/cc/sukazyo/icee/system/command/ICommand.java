package cc.sukazyo.icee.system.command;

import java.util.List;
import java.util.Map;

public interface ICommand {
	
	List<String> getRegistryName ();
	
	CommandType getType();
	
	void execute(String[] args, Map<String, String> parameters) throws CommandException;
	
	enum CommandType {
		
		/**
		 * 系统级别的命令<br>
		 * 这种命令会操作（读取或更改）系统中运行的资源
		 */
		SYSTEM_COMMAND,
		
		/**
		 * 辅助级别的命令<br>
		 * 这种命令会直接在CLI进程中执行，不调用系统进程
		 */
		HELPER_COMMAND,
		
		/**
		 * 多重类型的命令<br>
		 * 具体的执行逻辑将会以给定的剩余参数判断
		 */
		MULTIPLE_COMMAND;
		
		public char getPrefix () {
			switch (this) {
				case SYSTEM_COMMAND:
					return '@';
				case HELPER_COMMAND:
					return '>';
				case MULTIPLE_COMMAND:
					return '&';
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
