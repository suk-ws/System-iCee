package cc.sukazyo.icee.system;

import cc.sukazyo.icee.util.FileHelper;
import com.google.gson.Gson;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Scanner;

public class Proper {
	
	public static ProperTemplate.ConfTemplateSystem system = new ProperTemplate.ConfTemplateSystem();
	public static ProperTemplate.ConfTemplateBot bot = new ProperTemplate.ConfTemplateBot();
	
	public static void load () {
		
		Log.logger.info("Start loading properties");
		
		File sysConf = new File("./system.conf");
		File botConf = new File("./bot.conf");
		
		boolean waitStop = false;
		
		try {
			if (!sysConf.isFile()) {
				Log.logger.info("检测到没有配置文件，请等待系统生成默认配置文件后填写配置信息");
				summonConf(FileHelper.getNamePured(sysConf));
				waitStop = true;
			}
			if (!botConf.isFile()) {
				Log.logger.info("检测到 bot 配置缺失，请等待系统生成后进入填写");
				summonConf(FileHelper.getNamePured(botConf));
				waitStop = true;
			}
		} catch (IOException e) {
			Log.logger.error("Found unexcepted error while read properties.", e);
		}
		
		if (waitStop) {
			System.exit(0);
		}
		
		try {
			
			// 加载用户配置文件
			Scanner fscan = new Scanner(sysConf.toPath(), StandardCharsets.UTF_8.name());
			system = new Gson().fromJson(
					ConfigFactory.parseString(fscan.useDelimiter("\\A").next()).root().render(ConfigRenderOptions.concise()),
					ProperTemplate.ConfTemplateSystem.class);
			fscan = new Scanner(botConf.toPath(), StandardCharsets.UTF_8.name());
			bot = new Gson().fromJson(
					ConfigFactory.parseString(fscan.useDelimiter("\\A").next()).root().render(ConfigRenderOptions.concise()),
					ProperTemplate.ConfTemplateBot.class);
			
			try {
				BufferedInputStream ins = new BufferedInputStream(Proper.class.getResourceAsStream(
						"/assets/lang/icee_" + system.lang + ".lang"));
				ins.read();
				ins.close();
			} catch (IOException ee) {
				Log.logger.error("No this language support!!!", ee);
			}
			
			
			
		} catch (Exception e) {
			Log.logger.error("Found error while reading properties", e);
		}
		
		Log.logger.info("Properties load complete!");
		
	}
	
	private static void summonConf (String confTag) throws IOException {
		File iniFile = new File("./" + confTag + ".conf");
		BufferedInputStream ins = new BufferedInputStream(Proper.class.getResourceAsStream(
				"/assets/default/" + confTag + "_" + Locale.getDefault().toString().toLowerCase() + ".conf"));
		try {
			ins.mark(1);
			ins.read();
			ins.reset();
			Log.logger.info("Using your locale language to extract properties");
		} catch (IOException e) {
			ins.close();
			ins = new BufferedInputStream(Proper.class.getResourceAsStream(
					"/assets/default/" + confTag + ".conf"));
			Log.logger.info("Your locale language not support, using default en_us to extract properties");
		}
		if (iniFile.createNewFile()) {
			FileOutputStream os = new FileOutputStream(iniFile);
			Log.logger.info("Start Copy Default Properties");
			FileHelper.copyFile(ins, os);
			Log.logger.info("Summon default properties done.");
		} else {
			throw new IOException("Create " + confTag + ".conf Error");
		}
	}
	
}

