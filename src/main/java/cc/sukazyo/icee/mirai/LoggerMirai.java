package cc.sukazyo.icee.mirai;

import cc.sukazyo.icee.util.Log;
import net.mamoe.mirai.utils.MiraiLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LoggerMirai implements MiraiLogger {
	
	@Nullable
	@Override
	public MiraiLogger getFollower() {
		return null;
	}
	
	@Override
	public void setFollower(@Nullable MiraiLogger miraiLogger) {
	
	}
	
	@Nullable
	@Override
	public String getIdentity() {
		return null;
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	@Override
	public void debug(@Nullable String s) {
		Log.logger.debug(s);
	}
	
	@Override
	public void debug(@Nullable String s, @Nullable Throwable throwable) {
		Log.logger.debug(s, throwable);
	}
	
	@Override
	public void debug(@Nullable Throwable throwable) {
		Log.logger.debug("", throwable);
	}
	
	@Override
	public void error(@Nullable String s) {
		Log.logger.error(s);
	}
	
	@Override
	public void error(@Nullable String s, @Nullable Throwable throwable) {
		Log.logger.error(s, throwable);
	}
	
	@Override
	public void error(@Nullable Throwable throwable) {
		Log.logger.error("", throwable);
	}
	
	@Override
	public void info(@Nullable String s) {
		Log.logger.info(s);
	}
	
	@Override
	public void info(@Nullable String s, @Nullable Throwable throwable) {
		Log.logger.info(s, throwable);
	}
	
	@Override
	public void info(@Nullable Throwable throwable) {
		Log.logger.info("", throwable);
	}
	
	@NotNull
	@Override
	public <T extends MiraiLogger> T plus(@NotNull T t) {
		return null;
	}
	
	@Override
	public void plusAssign(@NotNull MiraiLogger miraiLogger) {
	
	}
	
	@Override
	public void verbose(@Nullable String s) {
		Log.logger.trace(s);
	}
	
	@Override
	public void verbose(@Nullable String s, @Nullable Throwable throwable) {
		Log.logger.trace(s, throwable);
	}
	
	@Override
	public void verbose(@Nullable Throwable throwable) {
		Log.logger.trace("", throwable);
	}
	
	@Override
	public void warning(@Nullable String s) {
		Log.logger.warn(s);
	}
	
	@Override
	public void warning(@Nullable String s, @Nullable Throwable throwable) {
		Log.logger.warn(s, throwable);
	}
	
	@Override
	public void warning(@Nullable Throwable throwable) {
		Log.logger.warn("", throwable);
	}
}
