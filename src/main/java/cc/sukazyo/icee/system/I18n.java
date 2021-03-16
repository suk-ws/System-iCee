package cc.sukazyo.icee.system;

import cc.sukazyo.icee.util.SimpleUtils;
import cc.sukazyo.icee.util.Var;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class I18n {
	
	/** 缺省语言<small>(默认为<code>en_us<code/>)</small>的本地化词条 */
	private static Properties common;
	/** 本地语言的本地化词条 */
	private static Properties local;
	
	/** 语言文件在资源文件的存放位置 */
	private static final String LANG_DIR = "lang";
	/** 缺省语言 (应该为<b><code>en_us</code></b>) */
	private static final String DEFAULT_LANG = "en_us";
	
	public static void init () {
		common = loadLang(DEFAULT_LANG);
		local = loadLang(Conf.conf.getString("system.lang"));
	}
	
	/**
	 * 将提供的文本节点转译为本地化文本<br>
	 * 文本节点的对应文本调用优先级为 <u>用户自定义目录 → 模块jar → 主程序jar → en_us/xxx</u>，右侧将会被左侧覆盖
	 * @param key 文本节点
	 * @return 本地化文本
	 */
	public static String get (String key) {
		return local.getProperty(key, common.getProperty(key, "${"+key+"}"));
	}
	
	/**
	 * 将提供的文本节点转译为本地化文本，再将本地化文本中的参数替换为指定的参数<br>
	 * <br>
	 * 例如以下语言文件定义：<br>
	 * <code>test.message_a=A Test with ${text}</code><br>
	 * 调用：<br>
	 * <code>I18n.get("test.message_a", new Var("text", "something"))</code><br>
	 * 将可以获得 <u>A Test with something</u><br>
	 * @see cc.sukazyo.icee.system.I18n#get(String) 本地化文本获取
	 * @param key 文本节点
	 * @param vars 文本参数
	 * @return 经过覆盖的本地化文本
	 */
	public static String get (String key, Var... vars) {
		String text = get(key);
		for (Var var : vars) {
			text = text.replaceAll(
					SimpleUtils.escapeExprSpecialWord("${" + var.key + "}"),
					var.value.replaceAll("\\$", "\\\\\\$") // 对于替换值存在 ${xxx} 格式的内容的兼容处理
			);
		}
		return text;
	}
	
	/**
	 * 完整加载一个语言的翻译<br/>
	 * 每一个翻译节点优先级为 <u>用户自定义目录 -> 模块jar -> 主程序jar</u>，右侧将会被左侧覆盖
	 *
	 * <br/><br/><font color="red"><b>目前未测试</b></font>
	 *
	 * @param lang 语言标识，规范为 <b><code>en_us</code></b>
	 * @return 此语言的当前完整翻译
	 */
	private static Properties loadLang (String lang) {
		Properties r = new Properties();
		try {
			Properties def = new Properties();
			def.load(new InputStreamReader(Resources.ASSETS_PACKAGE.getResource(LANG_DIR + "/" + lang + ".lang").read(), Resources.CHARSET));
			r.putAll(def);
		} catch (IOException ignored) {}
		Resources.MODULES_ASSETS.forEach(modPack -> {
			try {
				Properties def = new Properties();
				def.load(new InputStreamReader(modPack.getResource(LANG_DIR + "/" + lang + ".lang").read(), Resources.CHARSET));
				r.putAll(def);
			} catch (IOException ignored) { }
		});
		File customs = Resources.getCustomAssets(LANG_DIR + "/" + lang + ".lang");
		if (customs.isFile()) {
			try {
				Properties def = new Properties();
				def.load(new InputStreamReader(new FileInputStream(customs), Resources.CHARSET));
				r.putAll(def);
			} catch (IOException ignored) { }
		}
		return r;
	}
	
}
