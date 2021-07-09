package cc.sukazyo.icee.util.file;

import cc.sukazyo.icee.util.FileUtils;

import java.io.File;
import java.io.IOException;

public class NodeDirectory extends AbsNodeDirectory {
	
	private final AbsNodeDirectory superior;
	
	NodeDirectory (String name, AbsNodeDirectory superior) throws FileTreeException.FileNameUnavailableException {
		super(name);
		FileUtils.checkAvailableFileName(name);
		this.superior = superior;
	}
	
	@Override
	public String getFullName () {
		return String.format("%s/%s", superior.getFullName(), getName());
	}
	
	@Override
	public void createFile () throws IOException {
		if (!getFile().mkdirs()) {
			throw new IOException();
		}
	}
	
	@Override
	public File getFile () {
		return new File(superior.getFile(), getName());
	}
	
	@Override
	public boolean isExist () {
		return getFile().isDirectory();
	}
	
}
