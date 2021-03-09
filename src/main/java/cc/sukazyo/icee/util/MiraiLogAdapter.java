package cc.sukazyo.icee.util;

import net.mamoe.mirai.utils.MiraiLogger;
import net.mamoe.mirai.utils.MiraiLoggerPlatformBase;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public class MiraiLogAdapter extends MiraiLoggerPlatformBase implements MiraiLogger {
	
	private static final int BASE_DEPTH = 5;
	
	private final Logger logger;
	
	public MiraiLogAdapter (Logger logger) {
		this.logger = logger;
	}
	
	@Nullable
	@Override
	public String getIdentity () {
		return null;
	}
	
	@Override
	public void debug (@Nullable Throwable e) {
		StdLogAdapter.params(BASE_DEPTH);
		logger.debug(StdLogAdapter.MARK, "", e);
	}
	
	@Override
	public void error (@Nullable Throwable e) {
		StdLogAdapter.params(BASE_DEPTH);
		logger.error(StdLogAdapter.MARK, "", e);
	}
	
	@Override
	public void info (@Nullable Throwable e) {
		StdLogAdapter.params(BASE_DEPTH);
		logger.info(StdLogAdapter.MARK, "", e);
	}
	
	@Override
	public void verbose (@Nullable Throwable e) {
		StdLogAdapter.params(BASE_DEPTH);
		logger.trace(StdLogAdapter.MARK, "", e);
	}
	
	@Override
	public void warning (@Nullable Throwable e) {
		StdLogAdapter.params(BASE_DEPTH);
		logger.warn(StdLogAdapter.MARK, "", e);
	}
	
	@Override
	protected void debug0 (@Nullable String s, @Nullable Throwable throwable) {
		StdLogAdapter.params(BASE_DEPTH);
		logger.debug(StdLogAdapter.MARK, s, throwable);
	}
	
	@Override
	protected void error0 (@Nullable String s, @Nullable Throwable throwable) {
		StdLogAdapter.params(BASE_DEPTH);
		logger.error(StdLogAdapter.MARK, s, throwable);
	}
	
	@Override
	protected void info0 (@Nullable String s, @Nullable Throwable throwable) {
		StdLogAdapter.params(BASE_DEPTH);
		logger.info(StdLogAdapter.MARK, s, throwable);
	}
	
	@Override
	protected void verbose0 (@Nullable String s, @Nullable Throwable throwable) {
		StdLogAdapter.params(BASE_DEPTH);
		logger.trace(StdLogAdapter.MARK, s, throwable);
	}
	
	@Override
	protected void warning0 (@Nullable String s, @Nullable Throwable throwable) {
		StdLogAdapter.params(BASE_DEPTH);
		logger.warn(StdLogAdapter.MARK, s, throwable);
	}
	
}
