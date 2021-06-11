package cc.sukazyo.icee.system;

import cc.sukazyo.icee.system.config.Configure;
import cc.sukazyo.icee.util.SimpleUtils;
import cc.sukazyo.icee.util.TagAsException;
import cc.sukazyo.icee.util.Var;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class I18n {
	
	/** 简化不可识别的语言标记的正则识别器 */
	public static final Matcher langTagSimplify = Pattern.compile("^([\\s\\S]*?)[\\s\\S][a-zA-Z0-9]+$").matcher("");
	
	/** 语言索引配置中已声明的语言 */
	private static final Map<String, Localized> languages = new HashMap<>();
	static { languages.put(Localized.ROOT.langTag, Localized.ROOT); }
	/** 配置文件定义的当前语言 */
	private static Localized curr;
	/** 缓存当前的本地化调试开关状态 */
	private static boolean debug = false;
	
	/** 语言文件在资源文件的存放位置 */
	private static final String LANG_DIR = "lang";
	/** 语言文件后缀名 */
	private static final String LANG_FILE_EXTENSION = ".lang";
	/** 语言逻辑树&优先级索引文件名 */
	private static final String LANG_INDEX_FILENAME = "lang.conf";
	
	/**
	 * 单个语言的数据和语言树节点封装
	 */
	public static class Localized {
		
		/** 语言树的根节点 */
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
			addChildToSuperior(superior, priority);
			this.children = new LinkedList<>(); // 按照优先级降序排序
		}
		
		/**
		 * 一个翻译节点的值和来源的封装
		 */
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
		
		public String getLangTag () {
			return this.langTag;
		}
		
		/**
		 * 获取一个翻译节点的值封装<br/>
		 * 它会调用下面的在语言树中遍历查询的方法
		 * 如果整个语言树所有语言都没有值，则返回null
		 *
		 * @see Localized#forEach(Consumer) 根据树结构和优先级的遍历方法
		 *
		 * @param key 翻译节点
		 * @return 从语言树中找到的最近的对应值的封装对象，若不存在则返回null
		 */
		public Value get (String key) {
			AtomicReference<Value> rv = new AtomicReference<>();
			try {
				forEach(localized -> {
					if (localized.data.containsKey(key)) {
						rv.set(localized.data.get(key));
						throw TagAsException.INSTANCE;
					}
				});
			} catch (TagAsException ignored) {
				return rv.get();
			}
			return null;
		}
		
		/**
		 * 用于进行递归遍历整个树执行某动作<br/>
		 * 此方法会尝试首先在当前语言执行动作；
		 * 失败后会尝试以优先级从高到低为依据，依次向其子语言请求动作执行；
		 * 再次失败后会向其父语言请求动作执行。<br/>
		 * <br/>
		 * 可以安全使用 <code>TagAsException</code>
		 *
		 * @see Localized#load() 不同位置储存的同一个语言文件的优先级顺序
		 *
		 * @param action 遍历中执行的动作
		 */
		public void forEach (Consumer<Localized> action) {
			forEach(action, null);
		}
		
		private void forEach (Consumer<Localized> action, Localized ignored) {
			Objects.requireNonNull(action);
			action.accept(this);
			children.forEach((s) -> {
				if (s != ignored) {
					s.forEach(action, this);
				}
			});
			if (superior != ignored && superior != null)
				superior.forEach(action, this);
		}
		
		/**
		 * 返回当前语言中要求键的字符串字面值。<br/>
		 * 如果当前键没有对应值信息的话，将会返回<code>#{key%null}</code>
		 *
		 * @see Localized#get(String) 值来源
		 *
		 * @param key 要求的翻译键
		 * @return 对应字面值或者<code>${key}</code>(当没有对应值时)
		 */
		public String getText (String key) {
			return getText(key, String.format("#{%s%%null}", key));
		}
		
		/**
		 * 返回当前语言中要求键的字符串字面值。<br/>
		 * 如果当前键没有对应值信息的话，将会返回传入的缺省值
		 *
		 * @see Localized#get(String) 值来源
		 *
		 * @param key 要求的翻译键
		 * @param defaultValue 缺省值
		 * @return 对应字面值或者缺省值(当没有对应值时)
		 */
		public String getText (String key, String defaultValue) {
			Value v = get(key);
			return ( v!=null ? (
							debug ?
							String.format("#{%s%%%s.%s}", v.getValue(), v.getLang(), v.getSource()) :
							v.getValue()
					) : defaultValue);
		}
		
		/**
		 * 从磁盘完整加载当前语言的翻译<br/>
		 * 每一个翻译节点覆盖优先级为 <u>用户自定义目录 -> 模块jar -> 主程序jar</u>，右侧将会被左侧覆盖<br/>
		 * 同样也可用于从磁盘刷新翻译<br/>
		 * <br/>
		 * 同时也设置了每个翻译的来源信息<br/>
		 *
		 * <br/><br/><font color="red"><b>目前未进行完整测试</b></font>
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
		
		/**
		 * 设置当前语言的父语言<br/>
		 * 同时会对新旧父语言的子语言进行增删处理和排序
		 *
		 * @param superior 新的父语言
		 */
		public void setSuperior (Localized superior) {
			if (superior == this.superior) return;
			if (this.superior != null)
				this.superior.children.remove(this);
			addChildToSuperior(superior, priority);
		}
		
		/**
		 * 设置当前语言的优先级<br/>
		 * 同时会对新旧父语言的子语言进行增删处理和排序
		 *
		 * @param priority 新的优先级值
		 */
		public void setPriority (int priority) {
			if (priority == this.priority) return;
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
		
		private void addChildToSuperior (Localized superior, int priority) {
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
		
	}
	
	/**
	 * 将提供的文本节点转译为以当前设置语言为基准的本地化文本
	 *
	 * @see Localized#getText(String) 本地化文本获取细则
	 *
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
	 *
	 * @see Localized#getText(String) 本地化文本获取细则
	 *
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
	 * 以当前程序默认定义的语言为开始，
	 * 根据树结构和优先级的遍历整个语言树<br/>
	 * <br/>
	 * 可以安全使用 <code>TagAsException</code>
	 *
	 * @see Localized#forEach(Consumer)
	 * @param action 动作
	 */
	public static void forEach (Consumer<Localized> action) {
		forEachFrom(curr, action);
	}
	
	/**
	 * 根据树结构和优先级的遍历整个语言树<br/>
	 * <br/>
	 * 可以安全使用 <code>TagAsException</code>
	 *
	 * @see Localized#forEach(Consumer)
	 * @param action 动作
	 */
	public static void forEachFrom (Localized source, Consumer<Localized> action) {
		source.forEach(action);
	}
	
	/**
	 * 获取距离当前计算机所设定的语言最近的语言树中的语言
	 *
	 * @see Localized turnLocalized(String) 语言转换规则
	 * @return 距离当前计算机所设定的语言最近的语言树中的语言
	 */
	public static Localized getSystemLanguage () {
		return turnLocalized(Locale.getDefault().toString().toLowerCase());
	}
	
	/**
	 * 初始化已索引的语言内容数据<br/>
	 * 同时也可用于从硬盘刷新<br/>
	 * 不包含语言树的初始化，请提前刷新语言索引
	 *
	 * @see #index() 语言索引刷新方法
	 */
	public static void load () {
		
		languages.forEach((k, v) -> v.load()); // 加载语言的翻译
		
		// 设置当前本地化信息
		curr = Configure.getLanguage(Configure.CORE_ID, "system.lang.default");
		debug = Configure.getBoolean(Configure.CORE_ID, "system.lang.debug");
		
	}
	
	/**
	 * 从语言关系定义文件中索引已定义的语言<br/>
	 * 以 <u>用户自定义目录 -> 模块jar -> 主程序jar</u> 为优先级
	 *
	 * @throws ParseException 在读取文件或者解析文件时出现错误
	 */
	public static void index () throws ParseException {
		
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
		for (Map.Entry<Object, Object> entry : index.entrySet()) {
			String k = (String)entry.getKey();
			String v = (String)entry.getValue();
			Localized curr = languages.get(k);
			if (curr == null)
				throw new ParseException(String.format("Current language %s not found on language map while summon tree", k));
			String[] meta = (v).split("%");
			if (meta.length > 2)
				throw new ParseException(String.format("Too much language meta defined on %s", k));
			if (languages.containsKey(meta[0])) {
				curr.setSuperior(languages.get(meta[0]));
			} else
				throw new ParseException(String.format("The superior %s of %s is not a valid language", meta[0], k));
			try {
				curr.setPriority(meta.length > 1 ? Integer.parseInt(meta[1]) : 0);
			} catch (NumberFormatException e) {
				throw new ParseException(String.format("The priority of %s is defined as a non-numerical or too large value %s", meta[1], k));
			}
		}
		
		// 输出语言树
		final StringBuilder listLang = new StringBuilder("\nLocalization Tree::\n");
		Localized.ROOT.listChild(listLang, "|-");
		Log.logger.trace(listLang.substring(0, listLang.length()-1));
		
	}
	
	/**
	 * 获取语言树中距离传入的语言标签最近的的语言<br/>
	 * 这个方法会将传入的语言标签每次截除最后的一段
	 * 然后匹配树中是否有对应的语言
	 *
	 * @param originLanguageTag 需求的语言标签，要求小写下划线格式
	 * @return 已加载语言树中距离语言标签最近的语言对象
	 */
	public static Localized turnLocalized (String originLanguageTag) {
		while (true) {
			if (languages.containsKey(originLanguageTag)) {
				return languages.get(originLanguageTag);
			} else if (langTagSimplify.reset(originLanguageTag).matches()) {
				originLanguageTag = langTagSimplify.group(1);
			} else {
				return Localized.ROOT;
			}
		}
	}
	
	/**
	 * I18n 所使用的文件解析异常类
	 */
	public static class ParseException extends Exception {
		
		public ParseException (String message) {
			super(message);
		}
		
	}
	
}
