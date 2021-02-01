package cc.sukazyo.icee.system.command;

public class CommandException {
	
	public static class CommandNameExistException extends Exception { }
	
	public static class ParameterDuplicatedException extends Exception { }
	
	public static class CommandNotFoundException extends Exception {
		
		public CommandNotFoundException (String commandName) {
			super("Command \"" + commandName + "\" Not Found");
		}
		
	}
	
}
