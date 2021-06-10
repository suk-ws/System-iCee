package cc.sukazyo.icee.system.config;

public abstract class ConfigTypeException extends Exception {
	
	public ConfigTypeException (String message) {
		super(message);
	}
	
	public static class ValueOutOfRangeException extends ConfigTypeException {
		
		public ValueOutOfRangeException (String configKey, String configValue, String minimum, String maximum) {
			super(String.format(
					"Value of %s is %s, while the available range MUST be from %s to %s.",
					configKey,
					configValue,
					minimum,
					maximum
			));
		}
		
	}
	
	public static class WrongValueTypeException extends ConfigTypeException {
		
		public WrongValueTypeException (String configKey, String expectedType, String valueType) {
			super(String.format(
					"The value of %s is of type %s, and the expected value is %s.",
					configKey,
					valueType,
					expectedType
			));
		}
		
		public WrongValueTypeException (String configKey, String expectedType) {
			super(String.format(
					"The value of %s can't be format to type %s.",
					configKey,
					expectedType
			));
		}
		
		public WrongValueTypeException (String configKey) {
			super(String.format(
					"The value of %s is EMPTY!",
					configKey
			));
		}
		
	}
	
}
