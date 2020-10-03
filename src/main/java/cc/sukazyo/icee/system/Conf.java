package cc.sukazyo.icee.system;

import cc.sukazyo.icee.util.FileHelper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.*;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Conf {
	
	public static Config conf;
	
	private static Config def;
	
	static Pattern pattern;
	
	public static void load () {
		
		Log.logger.info("Start loading properties");
		
		// 加载默认配置文件
		try {
			Log.logger.debug(FileHelper.pack.getResource("default/icee.conf").readAsString());
			def = ConfigFactory.parseString(FileHelper.pack.getResource("default/icee.conf").readAsString());
		} catch (IOException e) {
			Log.logger.fatal("Default Config File Not Found, might the package had benn broken!", e);
			System.exit(4);
		}
		
		File sysConf = new File("./icee.conf");
		
		if (!sysConf.isFile()) {
			Log.logger.warn("检测到没有配置文件，请等待系统生成默认配置文件后填写配置信息");
			summonConf(FileHelper.getNamePured(sysConf));
			Log.logger.info("Summon properties done.");
			System.exit(0);
		}
		
		conf = ConfigFactory.parseFile(sysConf);
		
		// 版本检查
		if (conf.getInt("format") < def.getInt("format")) {
			Log.logger.warn("Config file out of date, updating...");
			summonConf(FileHelper.getNamePured(sysConf));
			Log.logger.info("Update properties done.");
		}
		
		System.exit(0);
		
		Log.logger.info("Properties load complete!");
		
	}
	
	private static void summonConf (String confTag) {
		File iniFile = new File("./" + confTag + ".conf");
		
		// 获取配置文件模板
		String template = null;
		try {
			template = FileHelper.pack.getResource("default/" + confTag + "_" + Locale.getDefault().toString().toLowerCase() + ".conf").readAsString();
		} catch (IOException e) {
			Log.logger.warn("Locale are not support on config " + confTag + ", using en_us to summon the default config file.");
			try {
				template = FileHelper.pack.getResource("default/" + confTag + "_" + Locale.getDefault().toString().toLowerCase() + ".conf").readAsString();
			} catch (IOException ioException) {
				Log.logger.fatal("en_us config template not found, might the package had benn broken!", e);
				System.exit(5);
			}
		}
		
		pattern = Pattern.compile("<<(.*?)>>");
		template = confTagReplace(template);
		pattern = null;
		Log.logger.debug(template);
		
		FileOutputStream os;
		try {
			os = new FileOutputStream(iniFile);
			os.write(template.getBytes());
		} catch (FileNotFoundException e) {
			Log.logger.fatal("Can't output config " + iniFile + " to the root dir!", e);
			if (iniFile.delete()) {
				Log.logger.info("Cleared error configure file.");
			}
			System.exit(1);
		} catch (IOException e) {
			Log.logger.fatal("Can't copy config " + iniFile, e);
			System.exit(3);
		}
		
	}
	
	private static String confTagReplace (String confs) {
		Matcher matcher = pattern.matcher(confs);
		if (matcher.find()) {
			Log.logger.debug(matcher.group(1));
			Log.logger.debug(def.getString(matcher.group(1)));
			if (conf != null && conf.hasPath(matcher.group(1)))
				confs = matcher.replaceFirst(conf.getString(matcher.group(1)));
			else
				confs = matcher.replaceFirst(def.getString(matcher.group(1)));
			confs = confTagReplace(confs);
		}
		return confs;
	}
	
}

