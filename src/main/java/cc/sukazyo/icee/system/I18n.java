package cc.sukazyo.icee.system;

import cc.sukazyo.icee.util.SimpleUtils;
import cc.sukazyo.icee.util.Var;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class I18n {
	
	public static final Matcher langTagSimplify = Pattern.compile("^([\\s\\S]*?)[a-zA-Z0-9]+$").matcher("");
	
	/** 语言索引配置中已声明的语言 */
	private static final Map<String, Localized> languages = new HashMap<>();
	static { languages.put(Localized.ROOT.langTag, Localized.ROOT); }
	private static Localized curr;
	
	/** 语言文件在资源文件的存放位置 */
	private static final String LANG_DIR = "lang";
	/** 语言文件后缀名 */
	private static final String LANG_FILE_EXTENSION = ".lang";
	/** 语言逻辑树&优先级索引文件名 */
	private static final String LANG_INDEX_FILENAME = "lang.conf";
	
	public static class Localized {
		
		public static final Localized ROOT = new Localized("root", null, 0);
		
		private final String langTag;
		private final Map<String, Value> data;
		private Localized superior;
		private final LinkedList<Localized> children;
		private int priority;
		
		private Localized (String langTag, Localized superior, int priority) {
			this.langTag = langTag;
			this.data = new HashMap<>();
			this.priority = priority;
			this.superior = superior;
			if (superior != null) {
				Iterator<Localized> it = superior.children.iterator();
				int itNum = 0;
				while (it.hasNext()) {
					if (it.next().priority < priority) {
						break;
					}
					itNum++;
				}
				superior.children.add(itNum, this);
			}
			this.children = new LinkedList<>(); // 按照优先级降序排序
		}
		
		public static class Value {
			
			private final String value;
			private final String lang;
			private final String source;
			
			Value (String value, String lang, String source) {
				this.value = value;
				this.lang = lang;
				this.source = source;
			}
			
			public String getValue () { return value; }
			
			public String getLang () { return lang; }
			
			public String getSource () { return source; }
			
		}
		
		public Value get (String key) {
			return get(key, null);
		}
		
		public Value get (String key, Localized ignored) {
			AtomicReference<Value> rv = new AtomicReference<>();
			AtomicBoolean got = new AtomicBoolean(false);
			if (data.containsKey(key)) {
				rv.set(data.get(key));
				got.set(true);
			} else children.forEach((s) -> {
				if (s != ignored) {
					Value vGot = s.get(key, this);
					if (vGot != null) {
						rv.set(vGot);
						got.set(true);
					}
				}
			});
			if (!got.get()) {
				if (superior != ignored)
					rv.set(superior.get(key, this));
			}
			return rv.get();
		}
		
		/**
		 * 返回当前语言中要求键的值。<br/>
		 * 如果当前键没有对应值信息的话，将会返回<code>${key}</code>
		 * @param key 键
		 * @return 对应值或者<code>${key}</code>(当没有对应值时)
		 */
		public String getText (String key) {
			return getText(key, String.format("${%s}", key));
		}
		
		/**
		 * 返回当前语言中要求键的值。<br/>
		 * 如果当前键没有对应值信息的话，将会返回缺省值
		 * @param key 键
		 * @param defaultValue 缺省值
		 * @return 对应值或者缺省值(当没有对应值时)
		 */
		public String getText (String key, String defaultValue) {
			Value v = get(key);
			return key!=null ? v.getValue() : defaultValue;
		}
		
		/**
		 * 从磁盘完整加载当前语言的翻译<br/>
		 * 每一个翻译节点优先级为 <u>用户自定义目录 -> 模块jar -> 主程序jar</u>，右侧将会被左侧覆盖<br/>
		 * 同样也可用于从磁盘刷新翻译
		 *
		 * <br/><br/><font color="red"><b>目前未测试</b></font>
		 *
		 */
		public void load () {
			try {
				Properties def = new Properties();
				def.load(new InputStreamReader(Resources.ASSETS_PACKAGE.getResource(LANG_DIR + "/" + langTag + LANG_FILE_EXTENSION).read(), Resources.CHARSET));
				def.forEach((k, v) -> this.data.put((String)k, new Value((String)v, langTag, "icee")));
			} catch (IOException ignored) {}
			Resources.MODULES_ASSETS.forEach(modPack -> {
				try {
					Properties def = new Properties();
					def.load(new InputStreamReader(modPack.getResource(LANG_DIR + "/" + langTag + LANG_FILE_EXTENSION).read(), Resources.CHARSET));
					def.forEach((k, v) -> this.data.put((String)k, new Value((String)v, langTag, "module:" + modPack)));
				} catch (IOException ignored) { }
			});
			File customs = Resources.getCustomAssets(LANG_DIR + "/" + langTag + LANG_FILE_EXTENSION);
			if (customs.isFile()) {
				try {
					Properties def = new Properties();
					def.load(new InputStreamReader(new FileInputStream(customs), Resources.CHARSET));
					def.forEach((k, v) -> this.data.put((String)k, new Value((String)v, langTag, "custom")));
				} catch (IOException ignored) { }
			}
		}
		
		public void setSuperior (Localized superior) {
			if (this.superior != null)
				this.superior.children.remove(this);
			this.superior = superior;
			if (superior != null) {
				Iterator<Localized> it = superior.children.iterator();
				int itNum = 0;
				while (it.hasNext()) {
					if (it.next().priority < priority) {
						break;
					}
					itNum++;
				}
				superior.children.add(itNum, this);
			}
		}
		
		public void setPriority (int priority) {
			if (superior != null) {
				superior.children.remove(this);
				Iterator<Localized> it = superior.children.iterator();
				int itNum = 0;
				while (it.hasNext()) {
					if (it.next().priority < priority) {
						break;
					}
					itNum++;
				}
				superior.children.add(itNum, this);
			}
			this.priority = priority;
		}
		
		private void listChild (StringBuilder output, String prefix) {
			output.append(prefix).append(langTag).append("%").append(priority).append("\n");
			String finalPrefix = "| " + prefix;
			children.forEach(child -> child.listChild(output, finalPrefix));
		}
		
	}
	
	/**
	 * 将提供的文本节点转译为本地化文本<br>
	 * 文本节点的对应文本调用优先级为 <u>用户自定义目录 → 模块jar → 主程序jar → en_us/xxx</u>，右侧将会被左侧覆盖
	 * @param key 文本节点
	 * @return 本地化文本
	 */
	public static String get (String key) {
		return curr.getText(key);
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
	
	public static void init () {
		
		// 加载本地化
		indexLanguages();
		final StringBuilder listLang = new StringBuilder("\nLocalization Tree::\n");
		Localized.ROOT.listChild(listLang, "|-");
		Log.logger.trace(listLang.substring(0, listLang.length()-1));
		languages.forEach((k, v) -> v.load());
		
		// 设置当前本地化信息
		String langDef = Conf.conf.getString("system.lang");
		while (true) {
			if (languages.containsKey(langDef)) {
				curr = languages.get(langDef);
				break;
			} else if (langTagSimplify.reset(langDef).matches()) {
				langDef = langTagSimplify.group(1);
				langDef = langDef.substring(0, langDef.length()-1);
			} else {
				curr = Localized.ROOT;
				break;
			}
		}
		
	}
	
	private static void indexLanguages () {
		
		// 从磁盘加载语言文件的索引配置
		Properties index = new Properties();
		try {
			Properties def = new Properties();
			def.load(new InputStreamReader(Resources.ASSETS_PACKAGE.getResource(LANG_DIR + "/" + LANG_INDEX_FILENAME).read(), Resources.CHARSET));
			index.putAll(def);
		} catch (IOException ignored) {}
		Resources.MODULES_ASSETS.forEach(modPack -> {
			try {
				Properties def = new Properties();
				def.load(new InputStreamReader(modPack.getResource(LANG_DIR + "/" + LANG_INDEX_FILENAME).read(), Resources.CHARSET));
				index.putAll(def);
			} catch (IOException ignored) { }
		});
		File customs = Resources.getCustomAssets(LANG_DIR + "/" + LANG_INDEX_FILENAME);
		if (customs.isFile()) {
			try {
				Properties def = new Properties();
				def.load(new InputStreamReader(new FileInputStream(customs), Resources.CHARSET));
				index.putAll(def);
			} catch (IOException ignored) { }
		}
		
		// 生成索引中所包含的语言
		index.keySet().forEach(_k -> {
			String k = (String)_k;
			if (!languages.containsKey(k))
				languages.put(k, new Localized(k, Localized.ROOT, 0));
		});
		
		// 构建依赖树
		index.forEach((_k, v) -> {
			String k = (String)_k;
			Localized curr = languages.get(k);
			if (curr == null)
				throw new RuntimeException("current language not found on language map while summon tree");// TODO curr null
			String[] meta = ((String)v).split("%");
			if (meta.length > 2)
				throw new RuntimeException("too much language meta defined."); // todo meta.length > 2
			if (languages.containsKey(meta[0])) {
				curr.setSuperior(languages.get(meta[0]));
			} else throw new RuntimeException("superior defined not found"); // todo null else
			curr.setPriority(meta.length>1 ? Integer.parseInt(meta[1]) : 0); // todo int parse exception
		});
		
	}
	
}
