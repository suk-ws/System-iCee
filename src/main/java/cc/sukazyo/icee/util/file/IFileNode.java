package cc.sukazyo.icee.util.file;

import java.io.File;
import java.io.IOException;

public interface IFileNode {
	
	String getName();
	
	String getFullName();
	
	void createFile() throws IOException;
	
	File getFile();
	
	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	boolean isExist();
	
}
