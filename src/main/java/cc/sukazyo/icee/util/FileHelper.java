package cc.sukazyo.icee.util;

import cc.sukazyo.icee.system.Resources;

import java.io.*;
import java.util.Scanner;

@SuppressWarnings("unused")
public class FileHelper {
	
	/**
	 * 从 InputStream 中读取字符串内容
	 *
	 * @param i InputStream
	 * @return 输入流中的字符串
	 */
	public static String getContentFromStream (InputStream i) {
		Scanner s = new Scanner(i, Resources.CHARSET.name());
		final String str = s.useDelimiter("\\A").next();
		s.close();
		return str;
	}
	
	/**
	 * 获取文件的不带后缀名的名称
	 *
	 * @param file 文件
	 * @return 无后缀文件名
	 */
	public static String getNamePured (File file) {
		return file.getName().substring(0, file.getName().lastIndexOf("."));
	}
	
}
