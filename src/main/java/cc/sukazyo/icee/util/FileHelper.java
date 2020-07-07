package cc.sukazyo.icee.util;

import cc.sukazyo.icee.system.Proper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

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
	
	public static String getResourcesContent (String path) throws IOException {
		
		BufferedInputStream ins = new BufferedInputStream(Proper.class.getResourceAsStream(path));
		byte[] buffer = new byte[1024];
		int bytesRead;
		StringBuilder chunk = new StringBuilder();
		//从文件中按字节读取内容，到文件尾部时read方法将返回-1
		while ((bytesRead = ins.read(buffer)) != -1)
		{
			//将读取的字节转为字符串对象
			chunk.append(new String(buffer, 0, bytesRead));
		}
		
		return chunk.toString();
		
	}
	
	public static String getDataContent (String path) throws IOException {
		Scanner in = new Scanner(new File(path).toPath(), StandardCharsets.UTF_8.name());
		String ret = in.useDelimiter("\\A").next();
		in.close();
		return ret;
	}
	
}
