package cc.sukazyo.icee.system;

import cc.sukazyo.icee.iCee;
import cc.sukazyo.icee.util.FileHelper;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.nio.channels.FileLock;
import java.nio.file.FileAlreadyExistsException;

public class InstanceManager {
	
	private static final File lockFile = new File("./.lock");
	private static FileLock lock;
	
	private static final File instance = new File("./.instance");
	
	public static boolean lock () {
		
		if (!lockFile.isFile())
			generateLockFile();
		
		try {
			lock = new RandomAccessFile(lockFile, "rws").getChannel().tryLock();
			if (lock == null || !lock.isValid())
				return false;
			RandomAccessFile iio = new RandomAccessFile(instance, "rw");
			iio.write(currentPID().getBytes());
			iio.close();
			return true;
		} catch (IOException e) {
			Log.logger.fatal("Generate instance information failed: ", e);
			iCee.exit(13);
		}
		
		return false;
		
	}
	
	public static void releaseLock () {
		if (lock == null || !lock.isValid()) {
			return;
		}
		try {
			if (!instance.delete()) Log.logger.warn("实例信息可能被由外部误操作！");
			lock.release();
		} catch (IOException ignored) { }
	}
	
	public static String currentPID () {
		return ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
	}
	
	public static String instancePID () {
		try {
			return FileHelper.getContentFromStream(new FileInputStream(instance));
		} catch (FileNotFoundException e) {
			return null;
		}
	}
	
	private static void generateLockFile () {
		
		try {
			if (!lockFile.createNewFile()) throw new FileAlreadyExistsException("Lock file already exists.");
		} catch (IOException e) {
			Log.logger.fatal("LockFile generate failed: ", e);
		}
		
	}
	
}
