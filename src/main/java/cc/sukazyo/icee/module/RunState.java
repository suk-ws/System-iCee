package cc.sukazyo.icee.module;

public enum RunState {
	
	OFF, STARTING, RUNNING;
	
	public boolean canStop () {
		return ordinal() >= RUNNING.ordinal();
	}
	
	public boolean canStart () {
		return ordinal() < STARTING.ordinal();
	}
	
}
