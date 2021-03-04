package cc.sukazyo.icee.system;

import cc.sukazyo.icee.iCee;
import cc.sukazyo.icee.util.FileHelper;
import cc.sukazyo.restools.ResourcesPackage;

import java.io.*;

public class Resources {
	
	private static final ResourcesPackage ASSETS_PACKAGE = new ResourcesPackage(iCee.class, "assets");
	private static final String ASSETS_DIR = "./assets/";
	
	private static final ResourcesPackage META = new ResourcesPackage(iCee.class, "meta");
	
	private static final String DATA_DIR = "./data/";
	
	private static File getCustomAssets (String path) {
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
	
	public static InputStream getAssets (String path) throws IOException {
		File customFile = getCustomAssets(path);
		if (customFile.isFile()) {
			try {
				return new FileInputStream(customFile);
			} catch (FileNotFoundException e) {
				Log.logger.error("Error occurred while reading custom assets file!", e);
			}
		}
		return ASSETS_PACKAGE.getResource(path).read();
	}
	
	public static String getAssetsAsString (String path) throws IOException {
		return FileHelper.getContentFromStream(getAssets(path));
	}
	
	public static InputStream getMetaFile (String path) throws IOException {
		return META.getResource(path).read();
	}
	
	public static String getMetaFileAsString (String path) throws IOException {
		return META.getResource(path).readAsString();
	}
	
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
	
	public static String getDataAsString (String path) throws IOException {
		return FileHelper.getContentFromStream(getData(path));
	}
	
}
