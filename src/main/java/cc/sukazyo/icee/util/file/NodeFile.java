package cc.sukazyo.icee.util.file;

import cc.sukazyo.icee.util.FileUtils;

import java.io.File;
import java.io.IOException;

public class NodeFile implements IFileNode {
	
	private final AbsNodeDirectory superior;
	private final String name;
	
	NodeFile (String name, AbsNodeDirectory superior) throws FileTreeException.FileNameUnavailableException {
		FileUtils.checkAvailableFileName(name);
		this.name = name;
		this.superior = superior;
	}
	
	@Override
	public String getName () {
		return name;
	}
	
	@Override
	public String getFullName () {
		return String.format("%s/%s", superior.getFullName(), getName());
	}
	
	@Override
	public void createFile () throws IOException {
		if (!superior.isExist()) {
			superior.createFile();
		}
		if (!getFile().createNewFile()) {
			throw new IOException();
		}
	}
	
	@Override
	public File getFile () {
		return new File(superior.getFile(), name);
	}
	
	@Override
	public boolean isExist () {
		return getFile().isFile();
	}
	
}
