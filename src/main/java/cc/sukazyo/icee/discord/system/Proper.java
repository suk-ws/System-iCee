package cc.sukazyo.icee.discord.system;

import cc.sukazyo.icee.discord.iCee;
import cc.sukazyo.icee.discord.util.Log;

import java.io.*;
import java.nio.file.Files;
import java.util.Properties;

public class Proper {
	
	public static String TOKEN;
	public static int logLevel = Log.DEBUG;
	public static boolean logDebug;
	
	static Properties ini = new Properties();
	
	public static void load () {
		
		File iniFile = new File("./properties.ini");
		
		try {
			if (!iniFile.isFile()) {
				Log.warn("检测到没有配置文件，请等待系统生成默认配置文件后填写配置信息");
				init(iniFile);
				System.exit(0);
			}
		} catch (IOException e) {
			System.err.println("[FATAL]Found unexcepted error while read properties.");
			e.printStackTrace();
			System.exit(1);
		}
		
		try {
			
			ini.load(new BufferedInputStream(new FileInputStream(iniFile)));
			
			TOKEN = ini.getProperty("token");
			
			if (iCee.DEBUG_MODE) {
				logLevel = Log.DEBUG;
				Log.debug("=======================================================");
				Log.debug("                   !!! Confirm !!!");
				Log.debug("             !!! You are being DEBUG MODE !!!");
				Log.debug("=======================================================");
			} else {
				switch (ini.getProperty("log.level")) {
					case "DEBUG":
						logLevel = Log.DEBUG;
						break;
					case "INFO":
						logLevel = Log.INFO;
						break;
					case "WARN":
						logLevel = Log.WARN;
						break;
					case "ERROR":
						logLevel = Log.ERROR;
						break;
					default:
						Log.error("Unsupported Log Level. Log level must be DEBUG/INFO/WARN/ERROR");
				}
			}
			
		} catch (Exception e) {
			Log.error("Found error while reading properties");
		}
		
	}
	
	private static void init (File iniFile) throws IOException {
		if (iniFile.createNewFile()) {
			iniFile.delete();
			FileOutputStream os = new FileOutputStream(iniFile);
			Log.info("Start Copy Default Properties");
			BufferedInputStream ins = new BufferedInputStream(Proper.class.getResourceAsStream("/properties.ini"));
			byte[] buf = new byte[4096];
			while ((ins.read(buf))!=-1) {
				os.write(buf);
			}
			os.close(); ins.close();
			Log.info("Summon default properties done.");
		} else {
			throw new IOException("Create properties.ini Error");
		}
	}
	
}
