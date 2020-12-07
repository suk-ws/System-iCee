package cc.sukazyo.icee.common;

public enum RunStatus {
	
	OFF, STOPPING, WAITING, STARTING, RESTARTING, ON;
	
	public boolean canStop () {
		return ordinal() > WAITING.ordinal();
	}
	
	public boolean canStart () {
		return ordinal() <= OFF.ordinal();
	}
	
	public static class StatusUnknownException extends RuntimeException {
		public StatusUnknownException (String message) { super(message); }
	}
	
}
