package cc.sukazyo.icee.util.file;

import cc.sukazyo.icee.util.FileUtils;

import java.io.File;
import java.io.IOException;

public class NodeRootDirectory extends AbsNodeDirectory {
	
	private final File root;
	
	public NodeRootDirectory (String path) {
		super(path);
		this.root = new File(path);
	}
	
	@Override
	public String getFullName () {
		return "";
	}
	
	@Override
	public void createFile () throws IOException {
		if (!getFile().mkdirs()) {
			throw new IOException();
		}
	}
	
	@Override
	public File getFile () {
		return root;
	}
	
	@Override
	public boolean isExist () {
		return getFile().isDirectory();
	}
	
	public NodeFile createNewFile (String path)
	throws FileTreeException.FileNameUnavailableException, FileTreeException.ConflictException {
		AbsNodeDirectory nodeCurr = this;
		String[] tmp = FileUtils.splitPath(path);
		for (int i = 0; i < tmp.length-1; i++) {
			if (!nodeCurr.hasChild(tmp[i])) {
				AbsNodeDirectory dir = new NodeDirectory(tmp[i], nodeCurr);
				nodeCurr.addChild(dir);
			}
		}
		NodeFile r = new NodeFile(tmp[tmp.length-1], nodeCurr);
		nodeCurr.addChild(r);
		return r;
	}
	
}
