package cc.sukazyo.icee.system;

import cc.sukazyo.icee.util.ConfigTypeHelper;
import cc.sukazyo.icee.util.FileHelper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;

import java.io.*;
import java.util.Locale;
import java.util.regex.Pattern;

public class Conf {
	
	public static Config conf;
	
	private static Config def;
	
	static Pattern pattern;
	
	public static void load () {
		
		Log.logger.info("Start loading Config");
		
		// 加载默认配置文件
		try {
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
			conf = ConfigFactory.parseFile(sysConf);
			Log.logger.info("Update properties done.");
		}
		
		// 数据检查
		def.getStringList("keys").forEach((key) -> {
			try {
				switch (def.getString(key + ".type")) {
					case "int": // 数值型数据
						ConfigTypeHelper.verifyInt(def, conf, key);
						break;
					case "long": // 长数值型
						ConfigTypeHelper.verifyLong(def, conf, key);
						break;
					case "boolean": // 布尔型
						ConfigTypeHelper.verifyBoolean(def, conf, key);
						break;
					case "tag": // 标签型
						ConfigTypeHelper.verifyTag(def, conf, key);
						break;
					case "string": // 字段型
						ConfigTypeHelper.verifyString(def, conf, key);
						break;
					case "ip" :
						ConfigTypeHelper.verifyIp(def, conf, key);
						break;
					case "ip-list":
						ConfigTypeHelper.verifyIpList(def, conf, key);
						break;
					default:
						throw new ConfigException.Parse(def.origin(), "Unsupported key type " + def.getString(key + ".type") + " define found on " + key);
				}
			} catch (ConfigException.WrongType e) {
				Log.logger.fatal(e.getMessage());
				System.exit(7);
			} catch (ConfigException.Missing e) {
				Log.logger.fatal("Missing Config " + key + "!", e);
				System.exit(6);
			} catch (ConfigException.Parse e) {
				Log.logger.fatal(e);
				System.exit(8);
			}
		});
		
		Log.logger.info("Config load complete!");
		
		if (conf.getBoolean("system.proxy.enable")) {
			System.setProperty("http.proxyHost", conf.getString("system.proxy.host"));
			System.setProperty("http.proxyPort", conf.getString("system.proxy.port"));
			System.setProperty("https.proxyHost", conf.getString("system.proxy.host"));
			System.setProperty("https.proxyPort", conf.getString("system.proxy.port"));
			StringBuilder bypassList = new StringBuilder();
			conf.getStringList("system.proxy.bypass").forEach((x) -> bypassList.append(" | ").append(x));
			System.setProperty("http.nonProxyHosts", bypassList.substring(3));
		}
		
		Log.logger.info("System configuration applied.");
		
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
				if (ConfigFactory.parseString(template).getInt("format") < def.getInt("format"))
					throw new Exception("Config Template Out of date");
			} catch (Exception exception) {
				Log.logger.fatal("en_us config template not found or out of date, might the package had benn broken!", exception);
				System.exit(5);
			}
		}
		
		pattern = Pattern.compile("<<(.*?)>>");
		for (String key : def.getStringList("keys")) {
			if (conf != null && conf.hasPath(key)) {
				try {
					template = template.replace("<<" + key + ">>", conf.getString(key));
				} catch (ConfigException.WrongType e) {
					template = template.replace("<<" + key + ">>", conf.getValue(key).render(ConfigRenderOptions.concise().setFormatted(true)));
				}
			} else {
				try {
					template = template.replace("<<" + key + ">>", def.getString(key + ".def"));
				} catch (ConfigException.WrongType e) {
					template = template.replace("<<" + key + ">>", def.getValue(key + ".def").render(ConfigRenderOptions.concise()));
				}
			}
		}
		pattern = null;
		
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

