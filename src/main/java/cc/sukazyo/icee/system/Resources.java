package cc.sukazyo.icee.system;

import cc.sukazyo.icee.iCee;
import cc.sukazyo.icee.util.FileHelper;
import cc.sukazyo.restools.ResourcesPackage;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Resources {
	
	/** 标准 Charset 引用 */
	public static final Charset CHARSET = StandardCharsets.UTF_8;
	
	/** iCee 主程序所附带的资源文件 */
	static final ResourcesPackage ASSETS_PACKAGE = new ResourcesPackage(iCee.class, "assets");
	/** 外置模块的资源文件表 */
	static final List<ResourcesPackage> MODULES_ASSETS = new ArrayList<>();
	/** 用户目录下的资源文件目录名 */
	static final String ASSETS_DIR = "./assets/";
	
	/** iCee 主程序配置文件 */
	private static final ResourcesPackage META = new ResourcesPackage(iCee.class, "meta");
	
	/** 用户数据存放目录名 */
	private static final String DATA_DIR = "./data/";
	
	/**
	 * 获取用户自定义的资源文件
	 *
	 * @param path 资源文件路径
	 * @return 用户定义的资源文件
	 */
	static File getCustomAssets (String path) {
		if (
				path.length() > 1 && (
						path.charAt(0) == '\\' ||
						path.charAt(0) == '/'
				)
		) {
			path = path.substring(1);
		}
		return new File(ASSETS_DIR + path);
	}
	
	/**
	 * 获取一个资源文件<br/>
	 * 资源文件将以 <u>用户自定义目录 -> 模块jar -> 主程序jar</u> 为优先顺序进行读取
	 *
	 * @param path 资源文件路径
	 * @return 资源文件的读取流
	 * @throws IOException 在读取时遇到错误（如文件不存在）
	 */
	public static InputStream getAssets (String path) throws IOException {
		File customFile = getCustomAssets(path);
		if (customFile.isFile()) {
			try {
				return new FileInputStream(customFile);
			} catch (FileNotFoundException e) {
				Log.logger.error("Error occurred while reading custom assets file!", e);
			}
		}
		for (ResourcesPackage modPack : MODULES_ASSETS) {
			try {
				return modPack.getResource(path).read();
			} catch (IOException ignored) { }
		}
		return ASSETS_PACKAGE.getResource(path).read();
	}
	
	/**
	 * 获取以字符串方式读取的纯文本资源文件<br/>
	 * 尝试用此方法读取非纯文本文件会造成乱码
	 *
	 * @see cc.sukazyo.icee.system.Resources#getAssets(String) getAssets(String path)
	 * @param path 资源文件路径
	 * @return 资源文件的字符串内容
	 * @throws IOException 在读取时遇到错误（如文件不存在）
	 */
	public static String getAssetsAsString (String path) throws IOException {
		return FileHelper.getContentFromStream(getAssets(path));
	}
	
	/**
	 * 获取 icee 主文件所带有的<s>元文件</s>配置文件
	 *
	 * @param path 文件在配置文件目录中的位置
	 * @return 文件读取流
	 * @throws IOException 读取文件时出现的错误
	 */
	public static InputStream getMetaFile (String path) throws IOException {
		return META.getResource(path).read();
	}
	
	/**
	 * 以纯文本字符串形式读取 icee 主文件所带有的<s>元文件</s>配置文件
	 *
	 * @param path 文件在配置文件目录中的位置
	 * @return 字符串文件内容
	 * @throws IOException 读取文件时出现的错误
	 */
	public static String getMetaFileAsString (String path) throws IOException {
		return META.getResource(path).readAsString();
	}
	
	/**
	 * 读取 iCee 运行时的数据文件<br/>
	 * 数据文件即生成的运行信息和储存数据等内容
	 *
	 * @param path 数据文件在数据目录下的位置
	 * @return 数据文件读取流
	 * @throws FileNotFoundException 没有此文件
	 */
	public static InputStream getData (String path) throws FileNotFoundException {
		if (
				path.length() > 1 && (
						path.charAt(0) == '\\' ||
						path.charAt(0) == '/'
				)
		) {
			path = path.substring(1);
		}
		return new FileInputStream(DATA_DIR + path);
	}
	
	/**
	 * 以纯文本字符串形式读取 iCee 运行时的数据文件<br/>
	 * 强制读取非纯文本文件会造成乱码
	 * 
	 * @see cc.sukazyo.icee.system.Resources#getData(String) getData(String) 
	 * @param path 数据文件在数据目录下的位置
	 * @return 数据文件读取流
	 * @throws FileNotFoundException 没有此文件
	 */
	public static String getDataAsString (String path) throws IOException {
		return FileHelper.getContentFromStream(getData(path));
	}
	
}
