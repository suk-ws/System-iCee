package cc.sukazyo.icee.system;

public class Exit extends Thread {
	
	private static final Exit EXIT_LISTENER = new Exit();
	
	private Exit() { super(); }
	
	@Override
	public void run () {
		InstanceManager.releaseLock();
	}
	
	public static void configureSafeExit() {
		if (Runtime.getRuntime().removeShutdownHook(EXIT_LISTENER)) {
				Log.logger.warn("The safe exit hook has been registered. done clean up and trying re-add.");
		}
		Runtime.getRuntime().addShutdownHook(EXIT_LISTENER);
	}
	
}
