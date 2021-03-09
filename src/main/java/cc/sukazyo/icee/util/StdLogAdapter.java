package cc.sukazyo.icee.util;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.ThreadContext;

import java.io.PrintStream;

public abstract class StdLogAdapter extends PrintStream {
	
	protected static final Marker MARK = MarkerManager.getMarker("ADAPT");
	
	protected final Logger logger;
	protected final int BASE_DEPTH = 3;
	
	public StdLogAdapter(Logger logger, PrintStream original) {
		super(original);
		this.logger = logger;
	}
	
	@Override
	public abstract void println(Object o);
	
	@Override
	public abstract void println(String s);
	
	public static void params(int baseDepth) {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		StackTraceElement elem = elements[baseDepth]; // The caller is always at BASE_DEPTH, including this call.
		if (elem.getClassName().startsWith("kotlin.io.")) {
			elem = elements[baseDepth + 2]; // Kotlin's IoPackage masks origins 2 deeper in the stack.
		} else if (elem.getClassName().startsWith("java.lang.Throwable")) {
			elem = elements[baseDepth + 4];
		}
		ThreadContext.put("class", elem.getClassName());
		ThreadContext.put("method", elem.getMethodName());
		ThreadContext.put("file", elem.getFileName());
		ThreadContext.put("line", String.valueOf(elem.getLineNumber()));
		ThreadContext.put(
				"location",
				String.format(
						"%s.%s(%s:%d)",
						elem.getClassName(),
						elem.getMethodName(),
						elem.getFileName(),
						elem.getLineNumber()
				)
		);
	}
	
	public static class StdOutLogAdapter extends StdLogAdapter {
		
		public StdOutLogAdapter (Logger logger, PrintStream original) {
			super(logger, original);
		}
		
		@Override
		public void println (Object o) {
			params(BASE_DEPTH);
			logger.info(MARK, "[STDOUT]"+o);
		}
		
		@Override
		public void println (String s) {
			params(BASE_DEPTH);
			logger.info(MARK, "[STDOUT]"+s);
		}
		
	}
	
	public static class StdErrLogAdapter extends StdLogAdapter {
		
		public StdErrLogAdapter (Logger logger, PrintStream original) {
			super(logger, original);
		}
		
		@Override
		public void println (Object o) {
			params(BASE_DEPTH);
			logger.error(MARK, "[STDERR]"+o);
		}
		
		@Override
		public void println (String s) {
			params(BASE_DEPTH);
			logger.error(MARK, "[STDERR]"+s);
		}
		
	}
	
}
