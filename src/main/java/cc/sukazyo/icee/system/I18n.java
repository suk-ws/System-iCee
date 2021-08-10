package cc.sukazyo.icee.system;

import cc.sukazyo.icee.system.config.Configure;
import cc.sukazyo.icee.util.TagAsException;
import cc.sukazyo.icee.util.Var;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
	/** 语言文件在资源文件的存放位置 */
	private static final String LANG_DIR = "lang";
	/** 语言文件后缀名 */
	private static final String LANG_FILE_EXTENSION = ".lang";
	/** 语言逻辑树&优先级索引文件名 */
	private static final String LANG_INDEX_FILENAME = "lang.conf";
	
	/** 语言索引配置中已声明的语言 */
	private static Localized root = Localized.getNewRoot();
	private static Map<String, Localized> languages = new HashMap<>();
	static { languages.put(Localized.ROOT_LANG_TAG, root); }
	/** 配置文件定义的当前语言 */
	private static Localized curr;
	/** 缓存当前的本地化调试开关状态 */
	private static boolean debug = false;
	
	/**
	 * 本地化定位数据类
	 * 包含（必选）翻译节点键，（可选）翻译参数，（可选）语言标签以及是否进行树遍历，（可选）默认值
	 */
	public static class Key {
		
		@Nonnull
		public final String key;
		@Nonnull
		public final Set<Var> vars;
		@Nullable
		public final String langTag;
		@Nullable
		public final String defaults;
		public final boolean isTraversal;
		
		/**
		 * 创建一个 I18n 本地化数据请求的数据封装对象
		 *
		 * @see Value#parse() 本地化参数的转换
		 *
		 * @param key 本地化数据节点（数据名
		 * @param defaults 默认值，当选定范围内没有数据时以此作为数据返回，默认为 null（parse将返回 {@code Value#toString()} 的结果）
		 * @param langTag 要求的语言，默认为 null（即使用icee系统语言）
		 * @param isTraversal 是否从要求语言开始遍历整个语言树，为false则只从 langTag 定义的单个语言中获取本地化值
		 * @param vars 本地化参数，在获取到本地化值时，parse将能够将本地化值中的参数位转换为参数值
		 */
		public Key (@Nonnull String key, @Nullable String defaults, @Nullable String langTag, boolean isTraversal, @Nonnull Var... vars) {
			this.key = key;
			this.defaults = defaults;
			this.langTag = langTag;
			this.isTraversal = isTraversal;
			this.vars = new TreeSet<>(Arrays.asList(vars));
		}
		
		/**
		 * 创建一个 I18n 本地化数据请求的数据封装对象<br/>
		 * <br/>
		 * 此方法为完整构建方法的默认值简略写法。
		 *
		 * @see #Key(String, String, String, boolean, Var...) 完整构建方法
		 *
		 * @param key 本地化数据节点（数据名
		 * @param vars 本地化参数，在获取到本地化值时，parse将能够将本地化值中的参数位转换为参数值
		 */
		public Key (@Nonnull String key, @Nonnull Var... vars) {
			this(key, null, null, true, vars);
		}
		
		/**
		 * 创建一个 I18n 本地化数据请求的数据封装对象<br/>
		 * <br/>
		 * 此方法为完整构建方法的另一个默认值简略写法。
		 *
		 * @see #Key(String, String, String, boolean, Var...) 完整构建方法
		 *
		 * @param key 本地化数据节点（数据名
		 * @param vars 本地化参数，在获取到本地化值时，parse将能够将本地化值中的参数位转换为参数值
		 */
		public Key (@Nonnull String key, @Nonnull String defaults, Var... vars) {
			this(key, defaults, null, true, vars);
		}
		
	}
	
	/**
	 * 返回数据封装
	 * 包含请求对象和查询到本地化节点
	 */
	public static class Value {
		
		public static final String RESP_NULL_LANG = Localized.RESERVED_TAGS[0];
		public static final String RESP_DEFAULT_LANG = Localized.RESERVED_TAGS[1];
		public static final String RESP_DEFAULT_SOURCE = "code";
		
		@Nonnull
		public final Key request;
		@Nullable
		public final Localized.Node response;
		
		public Value (@Nonnull Key req, @Nullable Localized.Node resp) {
			request = req;
			response = resp;
		}
		
		/**
		 * @return 应答到的语言，包含了 response node 为 null 时的 fallback 规则
		 */
		public String getResponseLang () {
			return response==null?(request.defaults==null?RESP_NULL_LANG:RESP_DEFAULT_LANG):response.lang;
		}
		
		/**
		 * @return 应答到的来源，包含了 response node 为 null 时的 fallback 规则
		 */
		public String getResponseSource () {
			return response==null?RESP_DEFAULT_SOURCE:response.source;
		}
		
		/**
		 * 根据I18n翻译请求和语言树返回的翻译节点生成本地化字符串<br/>
		 * <br/>
		 * 如果能够得到本地化值，则还会进行本地化值的参数替换操作：<br/>
		 * 例如以下语言文件定义：<br>
		 * <code>test.message_a=A Test with ${text}</code><br>
		 * 调用：<br>
		 * <code>I18n.get("test.message_a", new Var("text", "something"))</code><br>
		 * 将可以获得 <u>A Test with something</u><br>
		 * <br/>
		 * 如果没有能找到的翻译节点也没有默认值的情况下，
		 * 则会返回 {@link #toString()} 的返回值<br/>
		 * <br/>
		 * 如果 <code><u>config:core:system.lang.debug</u>==true</code>
		 * 则会将返回值format为 <u>{{@link #toString()}value}</u> 的格式
		 *
		 * @return 请求结果，或是fallback默认值，或者系统默认值 {@link #toString()}
		 */
		public String parse () {
			StringBuilder result = new StringBuilder();
			if (response != null) {
				result.append(response.getValue());
				String varTemp;
				int varIndex;
				for (Var var : request.vars) {
					varTemp = String.format("${%s}", var.key);
					while (true) {
						varIndex = result.indexOf(varTemp);
						if (varIndex==-1) break;
						result.replace(
								varIndex,
								varIndex + varTemp.length(),
								var.value.replaceAll("\\$", "\\\\\\$") // 对于替换值存在 ${xxx} 格式的内容的兼容处理
						);
					}
				}
			} else if (request.defaults != null) {
				result.append(request.defaults);
			} else {
				result.append(this);
			}
			if (debug && !(response==null && request.defaults == null))
				result.insert(0, this).insert(0, '{').append('}');
			return result.toString();
		}
		
		/**
		 * 将请求和其返回值进行格式化<br/>
		 * <br/>
		 * 格式化模板为 <u><code> #{key=数据值/!vd_eg=vd_eg.source/=param1="参数1的值", param2="参数2的值"} </code></u><br/><ul>
		 *     <li><code><u>=数据值</u></code> 只会在存在语言树返回值时出现</li>
		 *     <li>langTag 前的 <u><code>!</code></u> 符号只会在限定不遍历整个树的情况下存在</li>
		 *     <li>参数基于传入的request(Key)，如果没有参数，则 <u><code>/=xxx</code></u> 整个块不会出现</li>
		 * </ul>
		 *
		 * @see Localized#load() 来源字段的规则
		 *
		 * @return 格式化的返回数据概览
		 */
		@Override
		public String toString () {
			//前缀
			StringBuilder defaults = new StringBuilder("#{");
			// 数据
			defaults.append(request.key);
			if (response!=null) defaults.append('=').append(response.getValue());
			defaults.append('/');
			if (!request.isTraversal) defaults.append('!');
			// 语言和来源
			defaults.append(request.langTag);
			defaults.append('=').append(getResponseLang()).append('.').append(getResponseSource());
			// 参数
			defaults.append("/=");
			request.vars.forEach(var -> defaults.append(var.key).append("=\"").append(var.value).append("\", "));
			defaults.setLength(defaults.length()-2);
			// 尾缀
			defaults.append('}');
			return defaults.toString();
		}
		
	}
	
	/**
	 * 单个语言的数据和语言树节点封装
	 */
	public static class Localized {
		
		/** 语言标签规范 regex */
		public static final String TAG_STANDARD_REGEX = "^[a-zA-Z0-9_-]+$";
		/** 语言标签的保留字，用于特殊含义 */
		public static final String[] RESERVED_TAGS = {"null", "default"};
		public static final String[] WEAK_RESERVED_TAGS = {"root"};
		/** 语言标签保留字的集合，用于进行快速查询 */
		public static final Set<String> RESERVED_TAG_SET = new HashSet<>(Arrays.asList(RESERVED_TAGS));
		public static final Set<String> WEAK_RESERVED_TAG_SET = new HashSet<>(Arrays.asList(WEAK_RESERVED_TAGS));
		/** 语言树的根节点名 */
		public static final String ROOT_LANG_TAG = WEAK_RESERVED_TAGS[0];
		
		private final String langTag;
		private final Map<String, Node> data;
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
		 * 获取一个新的语言树根节点
		 * @return 全新的语言树根节点
		 */
		public static Localized getNewRoot () {
			return new Localized(ROOT_LANG_TAG, null, 0);
		}
		
		/**
		 * 一个翻译节点的值和来源的封装
		 */
		public static class Node {
			
			private final String value;
			private final String lang;
			private final String source;
			
			Node (String value, String lang, String source) {
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
		public Node get (String key) {
			AtomicReference<Node> rv = new AtomicReference<>();
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
		 * 获取一个翻译节点的值封装<br/>
		 * 它只会获取当前语言所属的值，不会进行全树遍历
		 *
		 * @param key 翻译节点
		 * @return 从语言树中找到的最近的对应值的封装对象，若不存在则返回null
		 */
		public Node getOnly (String key) {
			return data.get(key);
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
		 * 如果当前键没有对应值信息的话，将会返回<code>#{key/null}</code>
		 *
		 * @see Localized#get(String) 值来源
		 *
		 * @param key 要求的翻译键
		 * @return 对应字面值或者<code>${key}</code>(当没有对应值时)
		 */
		@Deprecated
		public String getText (String key) {
			return getText(key, String.format("#{%s/null}", key));
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
		@Deprecated
		public String getText (String key, String defaultValue) {
			Node v = get(key);
			return ( v!=null ? (
							debug ?
							String.format("#{%s/%s.%s}", v.getValue(), v.getLang(), v.getSource()) :
							v.getValue()
					) : defaultValue);
		}
		
		/**
		 * 从磁盘完整加载当前语言的翻译<br/>
		 * 每一个翻译节点覆盖优先级为 <u>用户自定义目录 -> 模块jar -> 主程序jar</u>，右侧将会被左侧覆盖<br/>
		 * 同样也可用于从磁盘刷新翻译<br/>
		 * <br/>
		 * 同时，此处也接管了来源字段的填写：<ul>
		 *     <li><u><code>core</code></u> icee核心所属的资源文件</li>
		 *     <li><u><code>module:xxx</code></u> 来源于模块jar xxx 的资源文件</li>
		 *     <li><u><code>custom</code></u> 来源于用户目录下 ./assets 文件夹下的资源文件</li>
		 * </ul>
		 * <br/>
		 * 同时也设置了每个翻译的来源信息<br/>
		 *
		 */
		public void load () {
			Log.logger.trace(">>Loading language {}...", langTag);
			try {
				Properties def = new Properties();
				def.load(new InputStreamReader(Resources.ASSETS_PACKAGE.getResource(LANG_DIR + "/" + langTag + LANG_FILE_EXTENSION).read(), Resources.CHARSET));
				def.forEach((k, v) -> this.data.put((String)k, new Node((String)v, langTag, "icee")));
				Log.logger.trace("succeed loading file at core.");
			} catch (IOException ignored) {}
			Resources.MODULES_ASSETS.forEach(modPack -> {
				try {
					Properties def = new Properties();
					def.load(new InputStreamReader(modPack.getResource(LANG_DIR + "/" + langTag + LANG_FILE_EXTENSION).read(), Resources.CHARSET));
					def.forEach((k, v) -> this.data.put((String)k, new Node((String)v, langTag, "module:" + modPack)));
					Log.logger.trace("succeed loading file at module [{}].", modPack.toString());
				} catch (IOException ignored) { }
			});
			File customs = Resources.getCustomAssets(LANG_DIR + "/" + langTag + LANG_FILE_EXTENSION);
			if (customs.isFile()) {
				try {
					Properties def = new Properties();
					def.load(new InputStreamReader(new FileInputStream(customs), Resources.CHARSET));
					def.forEach((k, v) -> this.data.put((String)k, new Node((String)v, langTag, "custom")));
					Log.logger.trace("succeed loading file at user assets pack.");
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
	 * 获取一个本地化值的数据本身（即get的超级简略写法，省略了两层对象包装，的再一层简略写法，又省略了一个参数~）<br/>
	 * <u>可能大多数情况下用这个就足够了，hae...</u>
	 *
	 * @see Key#Key(String, String, String, boolean, Var...) 参数用途
	 * @see Value#parse() 数据的处理方式
	 *
	 * @return 值
	 */
	public static String getText (String key, Var... vars) {
		return get(key, vars).parse();
	}
	
	/**
	 * 获取一个本地化值的数据本身（即get的超级简略写法，省略了两层对象包装）
	 *
	 * @see Key#Key(String, String, String, boolean, Var...) 参数用途
	 * @see Value#parse() 数据的处理方式
	 *
	 * @return 值
	 */
	public static String getText (String key, String defaults, Var... vars) {
		return get(key, defaults, vars).parse();
	}
	
	/**
	 * 获取一个本地化值的封装（的简略写法（其二））
	 *
	 * @see Key#Key(String, String, String, boolean, Var...) 参数用途
	 *
	 * @return 值的封装
	 */
	public static Value get (String key, String defaults, Var... vars) {
		return get(new Key(key, defaults, vars));
	}
	
	/**
	 * 获取一个本地化值的封装（的简略写法（其一））
	 *
	 * @see Key#Key(String, String, String, boolean, Var...) 参数用途
	 *
	 * @return 值的封装
	 */
	public static Value get (String key, Var... vars) {
		return get(new Key(key, vars));
	}
	
	/**
	 * 获取一个本地化值的封装
	 *
	 * @param request 请求键的封装
	 * @return 值的封装
	 */
	public static Value get (Key request) {
		
		Localized from;
		if (request.langTag == null) {
			from = curr;
		} else if (languages.containsKey(request.langTag)) {
			from = languages.get(request.langTag);
		} else {
			throw new RuntimeException("");
		}
		return new Value(request, request.isTraversal ? from.get(request.key) : from.getOnly(request.key));
		
	}
	
	public static Localized getLocalized (String langTag) {
		return languages.get(langTag);
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
	 * <b>不包含语言树的初始化，请提前刷新语言索引</b>
	 *
	 * @see #index() 语言索引刷新方法
	 */
	public static void load () {
		
		languages.forEach((k, v) -> v.load()); // 加载语言的翻译
		Log.logger.debug("All language load done.");
		
		// 设置当前本地化信息
		curr = Configure.getLanguage(Configure.CORE_ID, "system.lang.default");
		debug = Configure.getBoolean(Configure.CORE_ID, "system.lang.debug");
		Log.logger.debug("I18n load done.");
		
	}
	
	/**
	 * 从语言关系定义文件中索引已定义的语言<br/>
	 * 以 <u>用户自定义目录 -> 模块jar -> 主程序jar</u> 为优先级
	 *
	 * @throws ParseException 在读取文件或者解析文件时出现错误
	 */
	@SuppressWarnings("DuplicatedCode")
	public static void index () throws ParseException {
		
		Map<String, Localized> newLanguages = new HashMap<>();
		Localized newRoot = Localized.getNewRoot();
		newLanguages.put(Localized.ROOT_LANG_TAG, newRoot);
		
		// 从磁盘加载语言文件的索引配置
		Map<String, IndexNode> index = new HashMap<>();
		try {
			Log.logger.debug("Trying to load lang.conf in the core...");
			Scanner scanner = new Scanner(Resources.ASSETS_PACKAGE.getResource(LANG_DIR + "/" + LANG_INDEX_FILENAME).read(), Resources.CHARSET.name());
			int lineCounter = 0;
			while (scanner.hasNextLine()) {
				lineCounter++;
				try {
					IndexNode node = new IndexNode(scanner.nextLine());
					index.put(node.langTag, node);
				}
				catch (ParseException e) { throw new ParseException(String.format(
						"from icee-core line %d:\n\t%s",
						lineCounter, e.getMessage()
				)); }
			}
			Log.logger.debug("Done.");
		} catch (IOException e) {
			Log.logger.debug("Failed: ", e);
		}
		AtomicReference<String> errorMessage = new AtomicReference<>();
		try {
			Resources.MODULES_ASSETS.forEach(modPack -> {
				try {
					Scanner scanner = new Scanner(Resources.ASSETS_PACKAGE.getResource(LANG_DIR + "/" + LANG_INDEX_FILENAME).read(), Resources.CHARSET.name());
					int lineCounter = 0;
					while (scanner.hasNextLine()) {
						lineCounter++;
						try {
							IndexNode node = new IndexNode(scanner.nextLine());
							index.put(node.langTag, node);
						}
						catch (ParseException e) {
							errorMessage.set(String.format(
									"from module:%s line %d:\n\t%s",
									modPack.toString(), lineCounter, e.getMessage()
							));
							throw TagAsException.INSTANCE;
						}
						Log.logger.debug("Succeed loaded lang.conf at module [{}].", modPack.toString());
					}
				} catch (IOException ignored) { }
			});
		} catch (TagAsException tag) {
			throw new ParseException(errorMessage.get());
		}
		File customs = Resources.getCustomAssets(LANG_DIR + "/" + LANG_INDEX_FILENAME);
		if (customs.isFile()) {
			try {
				Scanner scanner = new Scanner(Resources.ASSETS_PACKAGE.getResource(LANG_DIR + "/" + LANG_INDEX_FILENAME).read(), Resources.CHARSET.name());
				int lineCounter = 0;
				while (scanner.hasNextLine()) {
					lineCounter++;
					try {
						IndexNode node = new IndexNode(scanner.nextLine());
						index.put(node.langTag, node);
					}
					catch (ParseException e) { throw new ParseException(String.format(
							"from assets-pack line %s:\n\t%s",
							lineCounter, e.getMessage()
					)); }
				}
				Log.logger.debug("Succeed loaded lang.conf at user assets pack.");
			} catch (IOException ignored) { }
		}
		
		// 生成索引中所包含的语言
		index.forEach((k, v) -> {
			if (!newLanguages.containsKey(k))
				newLanguages.put(k, new Localized(k, newRoot, 0));
		});
		
		// 构建依赖树
		Log.logger.debug("Parsing language tree...");
		for (Map.Entry<String, IndexNode> entry : index.entrySet()) {
			IndexNode node = entry.getValue();
			Localized curr = newLanguages.get(node.langTag);
			if (curr == null)
				throw new ParseException(String.format("Current language %s not found on language map while summon tree", node.langTag));
			if (newLanguages.containsKey(node.superior)) {
				curr.setSuperior(newLanguages.get(node.superior));
			} else throw new ParseException(String.format("The superior %s of %s is not a valid language", node.superior, node.langTag));
			curr.setPriority(node.priority);
		}
		
		// 写入与后处理
		root.forEach((data) -> {
			if (newLanguages.containsKey(data.getLangTag())) {
				newLanguages.get(data.getLangTag()).data.putAll(data.data);
			}
		});
		languages = newLanguages;
		root = newRoot;
		Log.logger.info("Language Tree Index Done.");
		
		// 输出语言树
		final StringBuilder listLang = new StringBuilder("Localization Tree::\n");
		root.listChild(listLang, "|-");
		Log.logger.debug(listLang.substring(0, listLang.length()-1));
		
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
				return root;
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
	
	private static class IndexNode {
		
		final String langTag;
		final String superior;
		final int priority;
		
		IndexNode (String line) throws ParseException {
			String[] data = line.split("=", 2);
			String[] meta = data[1].split("%");
			langTag = data[0];
			superior = meta[0];
			if (meta.length > 2)
				throw new ParseException("Too much language meta defined");
			try {
				priority = meta.length > 1 ? Integer.parseInt(meta[1]) : 0;
			} catch (NumberFormatException e) {
				throw new ParseException(String.format("The priority is defined as a non-numerical or too large value %s", meta[1]));
			}
			if (!langTag.matches(Localized.TAG_STANDARD_REGEX))
				throw new ParseException(String.format("Tag [%s] is not an available language tag format data", langTag));
			if (!superior.matches(Localized.TAG_STANDARD_REGEX))
				throw new ParseException(String.format("Tag [%s] is not an available language tag format data", superior));
			if (Localized.RESERVED_TAG_SET.contains(langTag) || Localized.WEAK_RESERVED_TAG_SET.contains(langTag))
				throw new ParseException(String.format("Reserved tag %s define found!", langTag));
			if (Localized.RESERVED_TAG_SET.contains(superior))
				throw new ParseException(String.format("Reserved tag %s usage found!", superior));
		}
		
	}
	
}
