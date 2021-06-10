package cc.sukazyo.icee.system.config;

public abstract class ConfigGetException extends RuntimeException {
	
	public ConfigGetException (String message) {
		super(message);
	}
	
	public static class MissingPage extends ConfigGetException {
		
		public MissingPage (String configPageId) {
			super(String.format(
					"Config page %s is not defined!",
					configPageId
			));
		}
		
	}
	
	public static class WrongType extends ConfigGetException {
		
		public WrongType (String configPageId, String configKey, String expectedType, String valueType) {
			super(String.format(
					"The value of of %s(%s)::%s is of %s, and the expected value is %s.",
					configPageId,
					Configure.getConfigPageSavePath(configPageId),
					configKey,
					valueType,
					expectedType
			));
		}
		
		public WrongType (String configPageId, String configKey, String expectedType) {
			super(String.format(
					"The value of %s(%s)::%s can't be format to type %s.",
					configPageId,
					Configure.getConfigPageSavePath(configPageId),
					configKey,
					expectedType
			));
		}
		
		public WrongType (String configPageId, String configKey) {
			super(String.format(
					"Missing %s on config page %s(%s)",
					configKey,
					configPageId,
					Configure.getConfigPageSavePath(configPageId)
			));
		}
		
	}
	
}
