package cc.sukazyo.icee.util;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleUtils {
	
	/**
	 * 将 regex 的特殊字符转译为实体字符
	 *
	 * @author https://blog.csdn.net/white__cat/article/details/53539314
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
	
	public static String generateListIndented (
			Collection<?> data, String header, String footer, String eol,
			String newlineDelimiter, String inlineDelimiter,
			int indentSize, boolean isUseTab, int spacingSize,
			boolean isFirstLineSpacing, boolean isLastNodeDelimited,
			boolean isCountNode, int lineNodeLimit, int lineCharLimit,
			boolean isCountNodeChar, int nodeCharLimit, boolean isIgnoreSymbol
	) {
		
		final StringBuilder result = new StringBuilder();
		final String indentLower = isUseTab?repeatChar('\t', indentSize):repeatChar(' ', indentSize*spacingSize);
		final String indent = isUseTab?repeatChar('\t', indentSize+1):repeatChar(' ', (indentSize+1)*spacingSize);
		final int inlineDelimiterLength = inlineDelimiter.length();
		result.append(isFirstLineSpacing?indentLower:"").append(header);
		AtomicInteger lineCounter = new AtomicInteger(Integer.MAX_VALUE);
		data.forEach(node -> {
			// 数据初始化
			String str = node.toString();
			int len = str.length();
			int realLen = len;
			if (isIgnoreSymbol) {
				if ( (str.startsWith("\"")&&str.endsWith("\"")) || (str.startsWith("'")&&str.endsWith("'")) )
					realLen -= 2;
				else if ( str.startsWith("-") && str.matches("^(0x)?[0-9a-f]+$") )
					realLen -= 1;
			}
			// 检测是否单个内容超过限制长度
			if (isCountNodeChar && realLen > nodeCharLimit) {
				result.append(newlineDelimiter).append(eol).append(indent).append(str);
				lineCounter.set(Integer.MAX_VALUE);
			}
			// 内容生成
			if (isCountNode) { // 以对象量为单位计算
				if (lineCounter.get() >= lineNodeLimit) {
					lineCounter.set(0);
					result.append(newlineDelimiter).append(eol).append(indent);
				} else {
					result.append(inlineDelimiter);
				}
				result.append(str);
				lineCounter.addAndGet(1);
			} else { // 以字节量为单位计算
				if (lineCounter.get() >= lineCharLimit) {
					lineCounter.set(0);
					result.append(newlineDelimiter).append(eol).append(indent);
				} else {
					result.append(inlineDelimiter);
					lineCounter.addAndGet(inlineDelimiterLength);
				}
				result.append(str);
				lineCounter.addAndGet(len);
			}
			result.append(eol).append(indent).append(node).append(newlineDelimiter);
		});
		
		// 删除段落首分隔符
		int headerCharLen = isFirstLineSpacing?indentLower.length()+header.length():header.length();
		result.delete(headerCharLen, headerCharLen+newlineDelimiter.length());
		// 添加段落尾分隔符
		if(!isLastNodeDelimited) {
			result.append(newlineDelimiter);
		}
		result.append(eol).append(footer);
		
		return result.toString();
		
	}
	
}
