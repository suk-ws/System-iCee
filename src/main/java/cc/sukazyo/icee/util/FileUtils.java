package cc.sukazyo.icee.util;

import cc.sukazyo.icee.system.Resources;
import cc.sukazyo.icee.util.file.FileTreeException;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

@SuppressWarnings("unused")
public class FileUtils {
	
	public static final String FILE_ERR_EMPTY = "empty filename";
	public static final String FILE_ERR_INVALID_SYMBOL = "unsupported character";
	public static final String FILE_ERR_INVALID_ENDING = "invalid ending character";
	public static final String FILE_ERR_ALMOST_EMPTY = "non physical character";
	
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
	
	/**
	 * 将一个文件路径字符串分割为目录数组<br/>
	 * <br/>
	 * 方法会按照普通的目录分隔符<code>/</code>和<code>\</code>来分割目录，
	 * 同时也支持<code>\\</code>的表示法。<br/>
	 * <br/>
	 * 同时，这个方法会直接丢弃掉前缀的<code>..../</code>一类的目录前缀。<br/>
	 * <br/>
	 * 如果出现了<code>//</code>或是<code>\\\\</code>这样的格式的目录，
	 * 这个方法将会将其识别为一个空名称的目录名而拒绝继续解析而是报错{@link cc.sukazyo.icee.util.file.FileTreeException.FileNameUnavailableException}。<br/>
	 * <br/>
	 * 同时，内嵌了{@link cc.sukazyo.icee.util.FileUtils#checkAvailableFileName(String)}的检查功能
	 *
	 * @param path 路径字符串
	 * @return 一个按照目录结构分割完成的数组
	 * @throws FileTreeException.FileNameUnavailableException 出现了无法解析的异常情况
	 */
	@SuppressWarnings("DuplicatedCode")
	public static String[] splitPath (String path) throws FileTreeException.FileNameUnavailableException {
		
		ArrayList<String> arr = new ArrayList<>();
		
		StringBuilder tmp = new StringBuilder();
		char[] coma = path.toCharArray();
		int start = 0;
		
		for (int i = 0; i < coma.length; i++) {
			if (coma[i] == '.') {
				continue;
			}
			if (coma[i] == '\\') {
				start = i+1;
				if (coma[i+1] == '\\') start = i+2;
				break;
			}
			if (coma[i] == '/') {
				start = i+1;
				break;
			}
			start = 0;
			break;
		}
		int spaces = 0;
		for (int i = start; i < coma.length; i++) {
			if (coma[i] == '\\') {
				if (tmp.length() == 0) {
					throw new FileTreeException.FileNameUnavailableException(path, i, FILE_ERR_EMPTY);
				} else if (spaces == tmp.length()) {
					throw new FileTreeException.FileNameUnavailableException(path, i, FILE_ERR_ALMOST_EMPTY);
				} else if (coma[i-1] == ' ' || coma[i-1] == '.') {
					throw new FileTreeException.FileNameUnavailableException(path, i-1, FILE_ERR_INVALID_ENDING);
				}
				arr.add(tmp.toString());
				tmp.setLength(0);
				spaces = 0;
				if (coma[i+1] == '\\') {
					i++;
				}
			} else if (coma[i] == '/') {
				if (tmp.length() == 0) {
					throw new FileTreeException.FileNameUnavailableException(path, i, FILE_ERR_EMPTY);
				} else if (spaces == tmp.length()) {
					throw new FileTreeException.FileNameUnavailableException(path, i, FILE_ERR_ALMOST_EMPTY);
				} else if (coma[i-1] == ' ' || coma[i-1] == '.') {
					throw new FileTreeException.FileNameUnavailableException(path, i-1, FILE_ERR_INVALID_ENDING);
				}
				arr.add(tmp.toString());
				tmp.setLength(0);
				spaces = 0;
			} else if (
					coma[i] == ':' || coma[i] == '*' ||
					coma[i] == '?' || coma[i] == '|' ||
					coma[i] == '"' || coma[i] == '\'' ||
					coma[i] == '>' || coma[i] == '<' ||
					coma[i] < 32
			) {
				throw new FileTreeException.FileNameUnavailableException(path, i, FILE_ERR_INVALID_SYMBOL);
			} else if (coma[i] == ' ') {
				spaces++;
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
	
	/**
	 * 检查传入的字符串是否是一个合格的文件名<br/>
	 *
	 * @param name 文件名
	 * @throws FileTreeException.FileNameUnavailableException 如果不合格，则抛出关于不合格的相关信息
	 */
	public static void checkAvailableFileName (String name) throws FileTreeException.FileNameUnavailableException {
		char[] chars = name.toCharArray();
		int spaces = 0;
		for (int i = 0; i < chars.length; i++) {
			if (
					chars[i] == '\\' || chars[i] == '/' ||
					chars[i] == ':' || chars[i] == '*' ||
					chars[i] == '?' || chars[i] == '|' ||
					chars[i] == '"' || chars[i] == '\'' ||
					chars[i] == '>' || chars[i] == '<' ||
					chars[i] < 32
			) {
				throw new FileTreeException.FileNameUnavailableException(name, i, FILE_ERR_INVALID_SYMBOL);
			} else if (chars[i] == ' ') {
				spaces++;
			}
		}
		if (spaces == chars.length) {
			throw new FileTreeException.FileNameUnavailableException(name, 0, FILE_ERR_ALMOST_EMPTY);
		}
		if (chars[chars.length-1] == ' ' || chars[chars.length-1] == '.') {
			throw new FileTreeException.FileNameUnavailableException(name, chars.length-1, FILE_ERR_INVALID_ENDING);
		}
	}
	
}
