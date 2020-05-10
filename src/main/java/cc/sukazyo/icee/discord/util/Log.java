package cc.sukazyo.icee.discord.util;

import cc.sukazyo.icee.discord.system.Proper;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
	
	public static final int DEBUG = 3;
	public static final int INFO = 2;
	public static final int WARN = 1;
	public static final int ERROR = 0;
	
	private static Writer file;
	private static Writer deb;
	
	private static boolean loaded = false;
	
	public static void init () {
		try {
			if ( !new File("./log").isDirectory() ) {
				Log.info("Log path not found, created.");
				new File("./log").mkdir();
			}
			file = new FileWriter("./log/[" + new File(new SimpleDateFormat("yyyy-MM-dd-HH·mm·ss").format(new Date()) + "]base.log"));
			deb = new FileWriter("./log/debug.log");
			loaded = true;
		} catch (IOException e) {
			System.err.println("[FATAL]Caught an error while create log file.");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private static void out (String info) {
		info = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]").format(new Date()) +
				"[" + Thread.currentThread().getId() + ":" + Thread.currentThread().getName() + ";t" + Thread.currentThread().getPriority() + "]" +
				info;
		System.out.println(info);
		if (loaded) {
			try {
				file.write(info + "\n");
				file.flush();
			} catch (IOException e) {
				System.err.println("[FATAL]Caught an error while writing log file.");
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
	
	private static void outDebug (String info) {
		if (loaded) {
			info = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]").format(new Date()) +
					"[" + Thread.currentThread().getId() + ":" + Thread.currentThread().getName() + ";t" + Thread.currentThread().getPriority() + "]" +
					info;
			try {
				deb.write(info + "\n");
				deb.flush();
			} catch (IOException e) {
				System.err.println("[FATAL]Caught an error while writing log file.");
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
	
	public static void debug (String info) {
		if (Proper.logLevel >= DEBUG) {
			out("[DEBUG]" + info);
		} if (Proper.logDebug) {
			outDebug("[DEBUG]" + info);
		}
	}
	
	public static void info (String info) {
		if (Proper.logLevel >= INFO) {
			out("[INFO]" + info);
		} if (Proper.logDebug) {
			outDebug("[DEBUG]" + info);
		}
	}
	
	public static void warn (String info) {
		if (Proper.logLevel >= WARN) {
			out("[WARN]" + info);
		} if (Proper.logDebug) {
			outDebug("[DEBUG]" + info);
		}
	}
	
	public static void error (String info) {
		if (Proper.logLevel >= ERROR) {
			out("[ERROR]" + info);
		} if (Proper.logDebug) {
			outDebug("[DEBUG]" + info);
		}
	}
	
}
