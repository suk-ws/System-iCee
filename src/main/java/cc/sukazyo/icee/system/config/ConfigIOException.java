package cc.sukazyo.icee.system.config;

public class ConfigIOException extends Exception {
	
	public ConfigIOException (String message) {
		super(message);
	}
	
	public ConfigIOException (String message, Exception e) {
		super(String.format("%s\n\t%s", message, e));
	}
	
}
