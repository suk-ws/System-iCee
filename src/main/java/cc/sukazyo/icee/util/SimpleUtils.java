package cc.sukazyo.icee.util;

import java.util.Random;

public class SimpleUtils {
	
	/**
	 * 将 regex 的特殊字符转译为实体字符
	 *
	 * 来源：
	 * @link origin https://blog.csdn.net/white__cat/article/details/53539314
	 *
	 * @param keyword 待转义串
	 * @return 转义完成，可供拼接regex的串
	 */
	public static String escapeExprSpecialWord(String keyword) {
		String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };
		for (String key : fbsArr) {
			if (keyword.contains(key)) {
				keyword = keyword.replace(key, "\\" + key);
			}
		}
		return keyword;
	}
	
	/**
	 * 随机一个 long 数字作为 id 返回
	 * @return 返回的Id
	 */
	public static long randomId () {
		return new Random().nextLong();
	}
	
	/**
	 * 检查提供的字符串是否为 ipv4 格式
	 *
	 * @param str 提供的字符串
	 * @return 是否为 ipv4
	 */
	public static boolean isIp (String str) {
		return str.matches("^((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}$");
	}
	
}
