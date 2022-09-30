package cc.sukazyo.icee.util;

import java.util.Collection;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("unused")
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
	
	/**
	 * 创建一个重复含有多个相同字符的字符串
	 *
	 * @param chr 源字符
	 * @param n 字符数量
	 * @return 创建的字符串
	 */
	public static String repeatChar (char chr, int n) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
			sb.append(chr);
		}
		return sb.toString();
	}
	
	/**
	 * 从传入的数据来生成一个带有缩进和换行的列表（比如这样的）：<br/>
	 * <code>something: [<br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;"a", "b", "c",<br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;"d", "e", "f"<br/>
	 * ]</code><br/>
	 * <br/>
	 * 可以通过以下的参数配置缩进或者是输出格式<br/>
	 * <br/>
	 * <s>doc还不是很完善</s>
	 *
	 * @param data 列表数据本体，通过 .toString() 方法进行输出
	 * @param header 表单头
	 * @param footer 表单尾
	 * @param eol 换行符（兼容性考虑）（你应该使用<code>\n</code>，特殊情况可以选择使用<code>\r\n</code>）
	 * @param dataPrefix 单个数据的前缀，例如 string 的 <code>"</code>
	 * @param dataSuffix 单个数据的后缀，例如 string 的 <code>"</code>
	 * @param newlineDelimiter 换行时的数据间分隔符
	 * @param inlineDelimiter 不换行情况下的数据间分隔符
	 * @param indentSize 缩进的量，以表单头和表单尾的缩进为标准
	 * @param isUseTab 是否使用 tab 缩进，如果为 false，则用下面的参数配置单个缩进有多少个空格
	 * @param spacingSize 单个缩进中有多少空格
	 * @param isFirstLineSpacing 首行是否需要带有缩进
	 * @param isLastNodeDelimited 最后的一个数据后是否需要加分隔符，
	 *     如果为 true 则会在最后一个数据后面加上<code>newlineDelimiter</code>
	 * @param isCountNode 选择是通过数据量还是输出的字符量进行换行判定
	 * @param lineNodeLimit 当<code>isCountNode</code>为<code>true</code>时生效，每行将会包含这个参数所定义的数量的数据
	 * @param lineCharLimit 当<code>isCountNode</code>为<code>false</code>时生效，每行将会包含这个参数所定义的数量的字符串，
	 *     字符数量计算不受<code>isIgnoreSymbol</code>影响，
	 *     同时也会计算<code>inlineDelimiter</code>和<code>dataPrefix</code>和<code>dataSuffix</code>的字符，
	 *     但是不会计算<code>newlineDelimiter</code>和<code>eol</code>的字符
	 * @param isCountNodeChar 是否启用每个数据的字符量检测
	 * @param nodeCharLimit 当<code>isCountNodeChar</code>为<code>true</code>时生效，
	 *     当单个数据的字符量超过这个数值时，将永远会在单独的一行输出。
	 *     同时，这个字符量不计算<code>dataPrefix</code>和<code>dataSuffix</code>中的字符
	 * @param isIgnoreSymbol 当<code>isCountNodeChar</code>为<code>true</code>时生效，
	 *     如果设置为<code>true</code>时，在<code>nodeCharLimit</code>计算字符量时，
	 *     会忽略字符串格式的前后引号或者是数字的前缀负号（如果有的话）
	 * @return 生成的列表字符串
	 */
	public static String generateListIndented (
			Collection<?> data, String header, String footer, String eol,
			String dataPrefix, String dataSuffix,
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
			// 内容生成
			if (isCountNodeChar && realLen > nodeCharLimit) { // 检测是否单个内容超过限制长度
				result.append(newlineDelimiter).append(eol).append(indent).append(dataPrefix).append(str).append(dataSuffix);
				lineCounter.set(Integer.MAX_VALUE);
			} else if (isCountNode) { // 以对象量为单位计算
				if (lineCounter.get() >= lineNodeLimit) {
					lineCounter.set(0);
					result.append(newlineDelimiter).append(eol).append(indent);
				} else {
					result.append(inlineDelimiter);
				}
				result.append(dataPrefix).append(str).append(dataSuffix);
				lineCounter.addAndGet(1);
			} else { // 以字符量为单位计算
				if (!(lineCounter.get() >= lineCharLimit)) lineCounter.addAndGet(len);
				if (lineCounter.get() >= lineCharLimit) {
					result.append(newlineDelimiter).append(eol).append(indent);
					lineCounter.set(0);
				} else {
					result.append(inlineDelimiter);
					lineCounter.addAndGet(inlineDelimiterLength);
				}
				result.append(dataPrefix).append(str).append(dataSuffix);
			}
		});
		
		// 删除段落首分隔符
		int headerCharLen = isFirstLineSpacing?indentLower.length()+header.length():header.length();
		result.delete(headerCharLen, headerCharLen+newlineDelimiter.length());
		// 添加段落尾分隔符
		if(isLastNodeDelimited) {
			result.append(newlineDelimiter);
		}
		result.append(eol).append(indentLower).append(footer);
		
		return result.toString();
		
	}
	
	/**
	 * @param mac 以数组形式存储的 mac 地址
	 * @return 小写字符串形式的 mac 地址
	 */
	public static String macByteToString (byte[] mac) {
		if (mac == null || mac.length != 6) {
			throw new IllegalArgumentException("MAC Address of byte array MUST be length of 6.");
		}
		StringBuilder address = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			int d = mac[i] - Byte.MIN_VALUE;
			if (d < 16) address.append('0');
			address.append(Integer.toHexString(d).toLowerCase());
			if (i != 5) address.append(':');
		}
		return address.toString();
	}
	
}
