package cc.sukazyo.icee.util;

/**
 * iCee 中用于在 lambda 中 break 的标记性异常<br/>
 * 记得 catch ！
 */
public class TagAsException extends RuntimeException {
	
	public static final TagAsException INSTANCE = new TagAsException();
	
	private TagAsException () {
		super("So, which one developer use a Tag but forget to catch it?");
	}
	
}
