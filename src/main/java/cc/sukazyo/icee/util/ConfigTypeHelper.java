package cc.sukazyo.icee.util;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;

public class ConfigTypeHelper {
	
	/**
	 * 检查是否为`boolean`类型
	 *
	 * @param defaults 要求配置文件 /unused/
	 * @param user 用户配置文件
	 * @param key 配置键
	 * @throws ConfigException.WrongType 类型检查失败
	 */
	@SuppressWarnings("unused")
	public static void verifyBoolean (Config defaults, Config user, String key) throws ConfigException.WrongType {
		user.getBoolean(key);
	}
	
	/**
	 * 检查是否为`int`类型，
	 * 同时检查给予的数值是否在要求数值范围内（左右不超出）
	 *
	 * @param defaults 要求配置文件
	 * @param user 用户配置文件
	 * @param key 配置键
	 * @throws ConfigException.WrongType 类型检查失败
	 */
	public static void verifyInt (Config defaults, Config user, String key) throws ConfigException.WrongType {
		user.getInt(key);
		if (defaults.hasPath(key + ".required") && (user.getInt(key) > defaults.getIntList(key + ".required").get(1) || user.getInt(key) < defaults.getIntList(key + ".required").get(0))) {
			throw new ConfigException.WrongType(user.origin(), "value of " + key + " (" + user.getString(key) + ") exceeds the required range ( " + defaults.getStringList(key+".required").toString() + ")");
		}
	}
	
	/**
	 * 检查是否为`long`类型，
	 * 同时检查给予的数值是否在要求数值范围内（左右不超出）
	 *
	 * @param defaults 要求配置文件
	 * @param user 用户配置文件
	 * @param key 配置键
	 * @throws ConfigException.WrongType 类型检查失败
	 */
	public static void verifyLong (Config defaults, Config user, String key) throws ConfigException.WrongType {
		user.getLong(key);
		if (defaults.hasPath(key + ".required") && (user.getLong(key) > defaults.getLongList(key + ".required").get(1) || user.getLong(key) < defaults.getLongList(key + ".required").get(0))) {
			throw new ConfigException.WrongType(user.origin(), "value of " + key + " (" + user.getString(key) + ") exceeds the required range ( " + defaults.getStringList(key+".required").toString() + ")");
		}
	}
	
	/**
	 * 检查是否为`string`类型
	 *
	 * @param defaults 要求配置文件 /unused/
	 * @param user 用户配置文件
	 * @param key 配置键
	 * @throws ConfigException.WrongType 类型检查失败
	 */
	@SuppressWarnings("unused")
	public static void verifyString (Config defaults, Config user, String key) throws ConfigException.WrongType {
		user.getString(key);
	}
	
	/**
	 * 检查用户提供的标签(`tag`)是否在定义要求中
	 *
	 * @param defaults 要求配置文件
	 * @param user 用户配置文件
	 * @param key 配置键
	 * @throws ConfigException.WrongType 类型检查失败
	 */
	public static void verifyTag (Config defaults, Config user, String key) throws ConfigException.WrongType {
		for (String s : defaults.getStringList(key + ".required")) {
			if (user.getString(key).equals(s)) return;
		}
		throw new ConfigException.WrongType(user.origin(), "value of " + key + " (" + user.getString(key) + ") is not supported!");
	}
	
	/**
	 * 检查是否为`ip区间`类型
	 *
	 * ip区间类型的要求详见
	 * @see SimpleUtils#isIpSection(String)
	 *
	 * @param defaults 要求配置文件 /unused/
	 * @param user 用户配置文件
	 * @param key 配置键
	 * @throws ConfigException.WrongType 类型检查失败
	 */
	public static void verifyIpSection (Config defaults, Config user, String key) throws ConfigException.WrongType {
		if (!SimpleUtils.isIpSection(user.getString(key))) {
			throw new ConfigException.WrongType(user.origin(), "value of " + key + " (" + user.getString(key) + ") is not a valid ip section!");
		}
	}
	
	/**
	 * 检查是否为一个ip区间列表
	 *
	 * ip区间类型的要求详见
	 * @see SimpleUtils#isIpSection(String)
	 *
	 * @param defaults 要求配置文件 /unused/
	 * @param user 用户配置文件
	 * @param key 配置键
	 * @throws ConfigException.WrongType 类型检查失败
	 */
	public static void verifyIpSectionList (Config defaults, Config user, String key) throws ConfigException.WrongType {
		for (String i : user.getStringList(key)) {
			if (!SimpleUtils.isIpSection(i)) {
				throw new ConfigException.WrongType(user.origin(), "value of " + key + " (" + i + ") is not a valid ip section!");
			}
		}
	}
	
}
