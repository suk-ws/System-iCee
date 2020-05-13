package cc.sukazyo.icee.discord.system;

import cc.sukazyo.icee.discord.iCee;
import cc.sukazyo.icee.discord.util.Log;

import java.io.*;
import java.util.Properties;

public class Proper {
	
	public static String TOKEN;
	public static int logLevel = Log.DEBUG;
	public static boolean logDebug = false;
	public static String lang = "en_us";
	
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
			Log.fatal("Found unexcepted error while read properties.", e);
		}
		
		try {
			
			ini.load(new BufferedInputStream(new FileInputStream(iniFile)));
			
			// 获取 Token
			TOKEN = ini.getProperty("token");
			
			// 获取 LogLevel
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
			
			// 获取 LogDebug
			switch (ini.getProperty("log.debugsave")) {
				case "true":
					logDebug = true;
					break;
				case "false":
					logDebug = false;
					break;
				default:
					Log.error("Unsupported Log Level. Debug Log must be true/false");
			}
			
		} catch (Exception e) {
			Log.error("Found error while reading properties");
			e.printStackTrace();
		}
		
		// 获取 Lang
		lang = ini.getProperty("lang");
		
	}
	
	private static void init (File iniFile) throws IOException {
		if (iniFile.createNewFile()) {
			iniFile.delete();
			FileOutputStream os = new FileOutputStream(iniFile);
			Log.info("Start Copy Default Properties");
			BufferedInputStream ins = new BufferedInputStream(Proper.class.getResourceAsStream("/default/properties.ini"));
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
