package cc.sukazyo.icee.util;

import java.io.*;

public class FileHelper {
	
	/**
	 * 从 InputStream 中读取字符串内容
	 *
	 * @param i InputStream
	 * @return 输入流中的字符串
	 * @throws IOException 读取失败
	 */
	public static String getContentFromStream (InputStream i) throws IOException {
		String str = null;
		byte[] data = new byte[i.available()];
		if (i.read(data) > -1) {
			str = new String(data);
		}
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
