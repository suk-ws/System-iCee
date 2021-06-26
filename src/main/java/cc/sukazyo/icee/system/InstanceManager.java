package cc.sukazyo.icee.system;

import cc.sukazyo.icee.iCee;
import cc.sukazyo.icee.util.FileHelper;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.nio.channels.FileLock;
import java.nio.file.FileAlreadyExistsException;
import java.util.Objects;

@SuppressWarnings("unused")
public class InstanceManager {
	
	private static final File lockFile = new File("./.lock");
	private static FileLock lock;
	
	private static final File instance = new File("./.instance");
	
	/**
	 * 尝试进行实例锁定，然后将当前进程信息记录进实例信息文件<br>
	 * 如果返回为 <b><code>true</code></b> 则实例锁获取成功<br>
	 * 如果返回为 <b><code>false</code></b> 则实例锁获取失败
	 * @return 锁定结果
	 */
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
			Log.logger.info("INSTANCE Locked.");
			return true;
		} catch (IOException e) {
			Log.logger.fatal("Generate instance information failed: ", e);
			iCee.exit(13);
		}
		
		return false;
		
	}
	
	/**
	 * 释放实例锁并取消配置(删除)实例信息文件
	 */
	public static void releaseLock () {
		if (lock == null || !lock.isValid()) {
			return;
		}
		try {
			if (!instance.delete()) Log.logger.warn("Might Instance Lock information been changed by external program!!!");
			lock.release();
		} catch (IOException ignored) { }
		Log.logger.info("INSTANCE unlocked.");
	}
	
	/**
	 * 获取当前进程的进程 ID<br>
	 * 进程 ID 为纯数字格式
	 * @return 当前进程的进程ID
	 */
	public static String currentPID () {
		return ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
	}
	
	/**
	 * 获取正在运行的实力的进程 ID<br>
	 * 返回值为<code>.instance</code>中记录的纯数字格式的字符串，
	 * 如果当前没有实例正在运行，则会返回 null
	 * @return 正在运行的实例进程 ID
	 */
	public static String instancePID () {
		try {
			return FileHelper.getContentFromStream(new FileInputStream(instance));
		} catch (FileNotFoundException e) {
			return null;
		}
	}
	
	/**
	 * 如果当前线程为此目录正在运行的实例的线程，则返回 true，
	 * 反之返回 false
	 * @return 当前进程是否为实例进程
	 */
	public static boolean isInstance () {
		return Objects.equals(instancePID(), currentPID());
	}
	
	private static void generateLockFile () {
		
		try {
			if (!lockFile.createNewFile()) throw new FileAlreadyExistsException("Lock file already exists.");
		} catch (IOException e) {
			Log.logger.fatal("LockFile generate failed: ", e);
		}
		
	}
	
}
