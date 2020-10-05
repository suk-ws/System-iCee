package cc.sukazyo.icee.system;

import cc.sukazyo.icee.util.FileHelper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;

import java.io.*;
import java.util.Locale;
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
		
		// 数据检查
		def.getStringList("keys").forEach((key) -> {
			if (!conf.hasPath(key)) {
				Log.logger.fatal("Missing Config " + key + "!");
				System.exit(6);
			}
			switch (def.getString(key + ".type")) {
				case "int":
					try {
						conf.getInt(key);
						if (def.hasPath(key + ".required") && (conf.getInt(key) > def.getIntList(key + ".required").get(1) || conf.getInt(key) < def.getIntList(key + ".required").get(0))) {
							Log.logger.fatal("Value of " + key + " is out of requirment");
							System.exit(7);
						}
					} catch (ConfigException.WrongType e) {
						Log.logger.fatal(e.getMessage());
						System.exit(7);
					}
					break;
				case "long":
					try {
						conf.getLong(key);
						if (def.hasPath(key + ".required") && (conf.getLong(key) > def.getLongList(key + ".required").get(1) || conf.getLong(key) < def.getLongList(key + ".required").get(0))) {
							Log.logger.fatal("Value of " + key + " is out of requirment");
							System.exit(7);
						}
					} catch (ConfigException.WrongType e) {
						Log.logger.fatal(e.getMessage());
						System.exit(7);
					}
					break;
				case "boolean":
					try {
						conf.getBoolean(key);
					} catch (ConfigException.WrongType e) {
						Log.logger.fatal(e.getMessage());
						System.exit(7);
					}
					break;
				case "tag":
					try {
						conf.getString(key);
						boolean isOK = false;
						for (String s : def.getStringList(key + ".required")) {
							if (conf.getString(key).equals(s)) isOK = true;
						}
						if (!isOK) {
							Log.logger.fatal("value of " + key + " are not supported!");
							System.exit(7);
						}
					} catch (ConfigException.WrongType e) {
						Log.logger.fatal(e.getMessage());
						System.exit(7);
					} catch (ConfigException.Missing ee) {
						Log.logger.fatal("Missing Tag Requirement on Config define file! Might the Application had been broken!");
						System.exit(8);
					}
					break;
				case "string":
					try {
						conf.getString(key);
					} catch (ConfigException.WrongType e) {
						Log.logger.fatal(e.getMessage());
						System.exit(7);
					}
					break;
				case "strlist" :
					try {
						conf.getStringList(key);
					} catch (ConfigException.WrongType e) {
						Log.logger.fatal(e.getMessage());
						System.exit(7);
					}
					break;
				default:
					Log.logger.warn("Unsupported key type " + def.getString(key + ".type") + " define found on " + key);
					System.exit(8);
			}
		});
		
		Log.logger.info("Properties load complete!");
		
		// Debug Exit Tag
//		System.exit(0);
		
	}
	
	private static void summonConf (String confTag) {
		File iniFile = new File("./" + confTag + ".conf");
		
		// 获取配置文件模板
		String template = null;
		try {
			template = FileHelper.pack.getResource("default/" + confTag + "_" + Locale.getDefault().toString().toLowerCase() + ".conf").readAsString();
			if (ConfigFactory.parseString(template).getInt("format") < def.getInt("format"))
				throw new Exception("Config Template Out of date");
		} catch (Exception e) {
			Log.logger.warn("Locale are not support on config " + confTag + ", using en_us to summon the default config file.");
			try {
				template = FileHelper.pack.getResource("default/" + confTag + "_en_us.conf").readAsString();
				Log.logger.debug(template);
				if (ConfigFactory.parseString(template).getInt("format") < def.getInt("format"))
					throw new Exception("Config Template Out of date");
			} catch (Exception exception) {
				Log.logger.fatal("en_us config template not found or out of date, might the package had benn broken!", exception);
				System.exit(5);
			}
		}
		
		pattern = Pattern.compile("<<(.*?)>>");
		for (String key : def.getStringList("keys")) {
			try {
				template = template.replace("<<" + key + ">>", def.getString(key + ".def"));
			} catch (ConfigException.WrongType e) {
				template = template.replace("<<" + key + ">>", def.getAnyRefList(key + ".def").toString());
			}
		}
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
			System.exit(2);
		} catch (IOException e) {
			Log.logger.fatal("Can't copy config " + iniFile, e);
			System.exit(3);
		}
		
	}
	
}

