package cc.sukazyo.icee.system.config;

public abstract class ConfigManageException extends Exception {
	
	public ConfigManageException (String message) {
		super(message);
	}
	
	public static class TypeConflictException extends ConfigManageException {
		
		public TypeConflictException (String typeName, IConfigType existing) {
			super(String.format(
					"Config type %s has been registered as %s",
					typeName,
					existing.getClass().getName()
			));
		}
		
	}
	
	public static class ConfigIdConflictException extends ConfigManageException {
		
		public ConfigIdConflictException (String id, String savePath) {
			super(String.format(
					"Config id %s has been registered (as %s).",
					id,
					savePath
			));
		}
		
	}
	
	public static class SavePathConflictException extends ConfigManageException {
		
		public SavePathConflictException (String savePath) {
			super(String.format(
					"Config save path %s has already be used.",
					savePath
			));
		}
		
	}
	
}
