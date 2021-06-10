package cc.sukazyo.icee.system.config;

public abstract class ConfigMetaException extends Exception {
	
	public ConfigMetaException (String message) {
		super(message);
	}
	
	public static class NoSuchTypeException extends ConfigMetaException {
		
		public NoSuchTypeException (String typeName, String atNode) {
			super(String.format("Config Type %s on %s is unavailable!", typeName, atNode));
		}
		
	}
	
	public static class WrongValueTypeException extends ConfigMetaException {
		
		public WrongValueTypeException (String configKey, String expectedType, String valueType) {
			super(String.format(
					"The type define of %s is of type %s, and the expected value is %s.",
					configKey,
					valueType,
					expectedType
			));
		}
		
		public WrongValueTypeException (int configKeyId, String expectedType, String valueType) {
			super(String.format(
					"The node define at location %d is of type %s, and the expected type is %s.",
					configKeyId,
					valueType,
					expectedType
			));
		}
		
		public WrongValueTypeException (String configKey) {
			super(String.format(
					"The type define of %s is EMPTY!",
					configKey
			));
		}
		
		public WrongValueTypeException (int configKeyId) {
			super(String.format(
					"The node define at location %d is EMPTY!",
					configKeyId
			));
		}
		
	}
	
}
