package cc.sukazyo.icee.discord.system;

import java.io.*;
import java.util.Properties;

public class Proper {
	
	public static String TOKEN;
	
	public static void init () {
		
		File iniFile = new File("./properties.ini");
		Properties ini = new Properties();
		
		try {
			if (!iniFile.isFile()) {
				iniFile.createNewFile();
				ini.setProperty("token", "<Enter Properties>");
				ini.store(new FileOutputStream(iniFile), "System iCee Default");
				// TODO 添加默认初始化系统
			}
		} catch (IOException e) {
			// TODO ERRORDUMP
		}
		
		try {
			
			ini.load(new BufferedInputStream(new FileInputStream(iniFile)));
			
			TOKEN = ini.getProperty("token");
			
		} catch (Exception e) {
			// TODO ERRORDUMP
		}
		
	}
	
}
