package cc.sukazyo.icee.util;

import java.util.*;

@SuppressWarnings("unused")
public class CommandHelper {
	
	/**
	 * 将一个字符串按照命令行的解析规则分割为参数数组<br/>
	 * <br/>
	 * 一般情况下，字符串会被按照出现的空格分割。
	 * 而如果出现引号（英文单引号和双引号(<code>'</code>|<code>"</code>)限定），将会将引号视作一个完整的字符串的开头或结尾控制字符，
	 * 这个完整的字符串中，将不会按照其中的空格进行分割。
	 * 而如果出现前导反斜线的引号(<code>\"</code>)，则会将这个引号（包含反斜线的整体）视为普通的字符看待而不会将其视作字符串的开头或结尾。
	 * 而如果出现前导反斜线的反斜线(<code>\\</code>)，则将会将这个反斜线视作普通的字符而不是「前导反斜线的引号的一部分」————
	 * 即，如果出现<code>\\"</code>，则反斜线会被正常输出，而引号会被认为是字符串的开头或结尾。
	 *
	 * @param com 字符串
	 * @return 解析出的参数数组
	 */
	public static String[] format (String com) {
		
		ArrayList<String> arr = new ArrayList<>();
		
		StringBuilder tmp = new StringBuilder();
		char[] coma = com.toCharArray();
		for (int i = 0; i < coma.length; i++) {
			if (coma[i] == ' ') {
				if (!tmp.toString().equals("")) { arr.add(tmp.toString()); }
				tmp.setLength(0);
			} else if (coma[i] == '"') {
				while (true) {
					i++;
					if (coma[i] == '"') {
						break;
					} else if (coma[i] == '\\' && (coma[i+1] == '"' || coma[i+1] == '\\')) {
						i++;
						tmp.append(coma[i]);
					} else {
						tmp.append(coma[i]);
					}
				}
			} else if (coma[i] == '\\' && (coma[i+1] == ' ' || coma[i+1] == '"' || coma[i+1] == '\\')) {
				i++;
				tmp.append(coma[i]);
			} else {
				tmp.append(coma[i]);
			}
		}
		if (!tmp.toString().equals("")) { arr.add(tmp.toString()); }
		tmp.setLength(0);
		
		String[] out = new String[arr.size()];
		arr.toArray(out);
		return out;
		
	}
	
}
