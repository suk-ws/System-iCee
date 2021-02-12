package cc.sukazyo.icee.system.command;

public class CommandException {
	
	public static class CommandNameConflictException extends Exception {
		
		public CommandNameConflictException (String commandName) {
			super("Command `" + commandName + "` have been registered.");
		}
		
	}
	
	public static class ParameterDuplicatedException extends Exception {
		
		public ParameterDuplicatedException (String param, String oldValue, String newValue) {
			super("Param `" + param + "` have already been set to `" + oldValue + "`, conflict with new value `" + newValue + "`!");
		}}
	
	public static class CommandNotFoundException extends Exception {
		
		public CommandNotFoundException (String commandName) {
			super("Command \"" + commandName + "\" Not Found");
		}
		
	}
	
}
