package cc.sukazyo.icee.system;

import cc.sukazyo.icee.util.Log;
import com.google.gson.Gson;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Scanner;

public class Proper {
	
	public static ProperTemplate user = new ProperTemplate();
	
	public static void load () {
		
		File iniFile = new File("./properties.conf");
		
		try {
			if (!iniFile.isFile()) {
				Log.logger.info("检测到没有配置文件，请等待系统生成默认配置文件后填写配置信息");
				init(iniFile);
				System.exit(0);
			}
		} catch (IOException e) {
			Log.logger.error("Found unexcepted error while read properties.", e);
		}
		
		try {
			
			// 加载用户配置文件
			Scanner fscan = new Scanner(iniFile.toPath(), StandardCharsets.UTF_8.name());
			user = new Gson().fromJson(
					ConfigFactory.parseString(fscan.useDelimiter("\\A").next()).root().render(ConfigRenderOptions.concise()),
					ProperTemplate.class);
			fscan.close();
			
			try {
				BufferedInputStream ins = new BufferedInputStream(Proper.class.getResourceAsStream(
						"/assets/lang/icee_" + user.lang + ".lang"));
				ins.read();
				ins.close();
			} catch (IOException ee) {
				Log.logger.error("No this language support!!!", ee);
			}
			
			
			
		} catch (Exception e) {
			Log.logger.error("Found error while reading properties", e);
		}
		
	}
	
	private static void init (File iniFile) throws IOException {
		BufferedInputStream ins = new BufferedInputStream(Proper.class.getResourceAsStream(
				"/assets/default/properties_" + Locale.getDefault().toString().toLowerCase() + ".conf"));
		try {
			ins.mark(1);
			ins.read();
			ins.reset();
			Log.logger.info("Using your locale language to extract properties");
		} catch (IOException e) {
			ins.close();
			ins = new BufferedInputStream(Proper.class.getResourceAsStream(
					"/assets/default/properties.conf"));
			Log.logger.info("Your locale language not support, using default en_us to extract properties");
		}
		if (iniFile.createNewFile()) {
			FileOutputStream os = new FileOutputStream(iniFile);
			Log.logger.info("Start Copy Default Properties");
			byte[] buf = new byte[1];
			while (ins.read(buf) != -1) {
				os.write(buf);
			}
			os.close(); ins.close();
			Log.logger.info("Summon default properties done.");
		} else {
			throw new IOException("Create properties.conf Error");
		}
	}
	
}

