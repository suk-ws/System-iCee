package cc.sukazyo.icee.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileHelper {
	
	public static void copyFile (InputStream source, OutputStream target) throws IOException {
		byte[] buf = new byte[1];
		while (source.read(buf) != -1) {
			target.write(buf);
		}
		source.close(); target.close();
	}
	
	public static String getNamePured (File file) {
		return file.getName().substring(0, file.getName().lastIndexOf("."));
	}
	
}
