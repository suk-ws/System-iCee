package cc.sukazyo.icee.system.command;

import java.util.Map;

public abstract class CommandException extends Exception {
	
	public CommandException (String message) { super(message); }
	
	public static class CommandNameConflictException extends CommandException {
		
		public CommandNameConflictException (String commandName) {
			super("Command `" + commandName + "` have been registered.");
		}
		
	}
	
	public static class ParameterDuplicatedException extends CommandException {
		
		public ParameterDuplicatedException (String param, String oldValue, String newValue) {
			super("Param `" + param + "` have already been set to `" + oldValue + "`, conflict with new value `" + newValue + "`!");
		}
		
		public ParameterDuplicatedException (String paramA, String paramB) {
			super("Param `" + paramA + "` conflicted with `"+ paramB + "`!");
		}
		
	}
	
	public static class CommandNotFoundException extends CommandException {
		
		private final String commandName;
		
		public CommandNotFoundException (String commandName) {
			super("Command \"" + commandName + "\" Not Found");
			this.commandName = commandName;
		}
		
		public String getCommandName () {
			return commandName;
		}
		
	}
	
	public static class ParameterUnsupportedException extends CommandException {
		
		public ParameterUnsupportedException (Map<String, String> unsupportedParams) {
			super(String.format(
					"Parameter%s \"%s\" %s not supported.",
					unsupportedParams.size()>1?"s":"",
					unsupportedParams.keySet().toString().substring(1, unsupportedParams.keySet().toString().length()-1),
					unsupportedParams.size()>1? "are" : "is")
			);
		}
		
		public ParameterUnsupportedException (String parameterName) {
			super(
					"Parameter" + parameterName + " is not supported."
			);
		}
		
	}
	
	public static class ParameterValueUnavailableException extends CommandException {
		
		public ParameterValueUnavailableException (String paramName, String value) {
			super(
					"Value of param " + paramName + " " +
					(value == null ? "<null>" : '"' + value + '"') +
					" is unavailable."
			);
		}
		
	}
	
	public static class ArgumentUnavailableException extends CommandException {
		
		public static final String EXCESSIVE_ARGUMENT = "excessive argument";
		
		public ArgumentUnavailableException (String arguments, String reason) {
			super(String.format(
					"Arguments [%s] is unavailable due to: %s",
					arguments, reason
			));
		}
		
	}
	
}
