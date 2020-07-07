package cc.sukazyo.icee.util;

import java.util.Random;

public class SimpleUtils {
	
	/**
	 * 将 regex 的特殊字符转译为实体字符
	 *
	 * 来源：
	 * @link https://blog.csdn.net/white__cat/article/details/53539314
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
	 * @return 返回的Id
	 */
	public static long randomId () {
		return new Random().nextLong();
	}
	
}
