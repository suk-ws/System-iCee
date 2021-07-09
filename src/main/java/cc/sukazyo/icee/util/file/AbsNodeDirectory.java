package cc.sukazyo.icee.util.file;

import java.util.HashMap;
import java.util.Map;

public abstract class AbsNodeDirectory implements IFileNode {
	
	private final String name;
	private final Map<String, IFileNode> children = new HashMap<>();
	
	protected AbsNodeDirectory (String name) {
		this.name = name;
	}
	
	@Override
	public String getName () {
		return name;
	}
	
	public void addChild (IFileNode child) throws FileTreeException.ConflictException {
		if (children.containsKey(child.getName())) {
			throw new FileTreeException.ConflictException();
		}
		children.put(child.getName(), child);
	}
	
	public boolean hasChild (String name) {
		return children.containsKey(name);
	}
	
	public IFileNode getChild (String name) {
		return children.get(name);
	}
	
}
