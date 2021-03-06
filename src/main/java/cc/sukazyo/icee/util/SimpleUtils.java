package cc.sukazyo.icee.util;

import java.util.Random;

public class SimpleUtils {
	
	/**
	 * 将 regex 的特殊字符转译为实体字符
	 *
	 * @link 来源 origin https://blog.csdn.net/white__cat/article/details/53539314
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
	
	/**
	 * 检查提供的字符串是否为 ipv4 格式或 ipv4 区间段
	 *
	 * @param str 提供的字符串
	 * @return 是否满足要求
	 */
	public static boolean isIpSection (String str) {
		String[] sections = str.split("\\.");
		if (sections.length < 5) {
			for (int i = 0; i < sections.length; i++) {
				if ("*".equals(sections[i])) {
					return i == sections.length - 1;
				} else if (!sections[i].matches("^((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})$")) return false;
			}
			return sections.length == 4;
		} return false;
	}
	
	public static String repeatChar (char chr, int n) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
			sb.append(chr);
		}
		return sb.toString();
	}
	
}
