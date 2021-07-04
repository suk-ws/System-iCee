package cc.sukazyo.icee.system.config;

import cc.sukazyo.icee.iCee;
import cc.sukazyo.icee.system.I18n;
import cc.sukazyo.icee.system.Log;
import cc.sukazyo.icee.system.Resources;
import cc.sukazyo.icee.system.config.IConfigType.IConfigValue;
import cc.sukazyo.icee.system.config.common.*;
import cc.sukazyo.icee.util.FileHelper;
import cc.sukazyo.icee.util.TagAsException;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValueType;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 目前仍含有一些暂时不明所以的 to-do 标签
 */
public class Configure {
	
	public static final String CORE_ID = "core";
	public static final String CORE_BOT_ID = "core-bot";
	public static final String LANG_CONFIG_PAGE_ID = CORE_ID;
	public static final String LANG_CONFIG_KEY = "system.lang.default";
	
	public static final String CONFIG_META_VERSION_TAG = "version";
	public static final String CONFIG_META_DATA_TAG = "configurations";
	
	public static final String USER_CONFIG_PATH = "./config/";
	public static final String CONFIG_META_FILENAME = "config.json";
	public static final String CONFIG_FILE_EXTENSION = ".conf";
	
	private static final Map<String, IConfigType> configureTypes = new HashMap<>();
	
	private static final Map<String, String> registeredConfigMeta = new HashMap<>();
	private static final Map<String, Map<String, IConfigValue>> loadedConfig = new HashMap<>();
	
	private static I18n.Localized defaultLanguage;
	
	public static IConfigValue get (String configPageId, String key) {
		Map<String, IConfigValue> page = loadedConfig.get(configPageId);
		if (page == null) throw new ConfigGetException.MissingPage(configPageId);
		IConfigValue value = page.get(key);
		if (value == null) throw new ConfigGetException.WrongType(configPageId, key);
		return page.get(key);
	}
	
	@SuppressWarnings("unused")
	public static int getInt (String configPageId, String key) {
		IConfigValue value = get(configPageId, key);
		if (value instanceof ConfigTypeInt.Value) return ((ConfigTypeInt.Value)value).get();
		throw new ConfigGetException.WrongType(configPageId, key, "int", value.getType().getTypeName());
	}
	
	@SuppressWarnings("unused")
	public static long getLong (String configPageId, String key) {
		IConfigValue value = get(configPageId, key);
		if (value instanceof ConfigTypeLong.Value) return ((ConfigTypeLong.Value)value).get();
		throw new ConfigGetException.WrongType(configPageId, key, "long", value.getType().getTypeName());
	}
	
	@SuppressWarnings("unused")
	public static String getString (String configPageId, String key) {
		IConfigValue value = get(configPageId, key);
		if (value instanceof ConfigTypeString.Value) return ((ConfigTypeString.Value)value).get();
		throw new ConfigGetException.WrongType(configPageId, key, "string", value.getType().getTypeName());
	}
	
	@SuppressWarnings("unused")
	public static String[] getStringList (String configPageId, String key) {
		IConfigValue value = get(configPageId, key);
		if (value instanceof ConfigTypeStringList.Value) return ((ConfigTypeStringList.Value)value).get().toArray(new String[0]);
		throw new ConfigGetException.WrongType(configPageId, key, "string-list", value.getType().getTypeName());
	}
	
	@SuppressWarnings("unused")
	public static boolean getBoolean (String configPageId, String key) {
		IConfigValue value = get(configPageId, key);
		if (value instanceof ConfigTypeBoolean.Value) return ((ConfigTypeBoolean.Value)value).get();
		throw new ConfigGetException.WrongType(configPageId, key, "boolean", value.getType().getTypeName());
	}
	
	@SuppressWarnings("unused")
	public static I18n.Localized getLanguage (String configPageId, String key) {
		IConfigValue value = get(configPageId, key);
		if (value instanceof ConfigTypeLanguage.Value) return ((ConfigTypeLanguage.Value)value).get();
		throw new ConfigGetException.WrongType(configPageId, key, "language", value.getType().getTypeName());
	}
	
	/**
	 * 注册一个自定义的配置节点类型
	 *
	 * @param resolver 此类型的解析器的实例对象
	 * @throws cc.sukazyo.icee.system.config.ConfigManageException.TypeConflictException
	 * 如果需要注册的配置节点类型已经被注册过则会报错此错误
	 */
	public static void registerConfigType (IConfigType resolver) throws ConfigManageException.TypeConflictException {
		Log.logger.trace(
				"config type {} (known as {}) got registered",
				resolver.getClass().getName(), resolver.getTypeName()
		);
		if (configureTypes.containsKey(resolver.getTypeName())) {
			throw new ConfigManageException.TypeConflictException(
					resolver.getTypeName(),
					configureTypes.get(resolver.getTypeName())
			);
		}
		configureTypes.put(resolver.getTypeName(), resolver);
	}
	
	public static String getConfigPageSavePath (String configPageId) {
		return registeredConfigMeta.get(configPageId);
	}
	
	/**
	 * 注册一个需要被生成和加载的配置文件<br/>
	 * <br/>
	 * 配置文件保存的路径基于"./data"，传入的路径不应该有前导 "/" 和后缀 ".conf"。<br/>
	 * 例如，如果希望配置文件位于 <u><code>./data/custom-config/test-a.conf</code></u> ，
	 * 则传入的路径应该写为 <u><code>custom-config/test-a</code></u> 。
	 *
	 * @param configPageId 配置文件id
	 * @param savePath 配置文件应该被保存的路径。
	 * @throws cc.sukazyo.icee.system.config.ConfigManageException.ConfigIdConflictException
	 * 需要注册的配置文件ID已经存在
	 * @throws cc.sukazyo.icee.system.config.ConfigManageException.SavePathConflictException
	 * 需要注册的配置文件保存位置已被占用
	 */
	public static void registerConfig (String configPageId, String savePath)
	throws ConfigManageException.ConfigIdConflictException, ConfigManageException.SavePathConflictException {
		Log.logger.trace("config page {}({}) got registered", configPageId, savePath);
		if (registeredConfigMeta.containsKey(configPageId)) {
			throw new ConfigManageException.ConfigIdConflictException(
					configPageId,
					registeredConfigMeta.get(configPageId)
			);
		} else if (registeredConfigMeta.containsValue(savePath)) {
			throw new ConfigManageException.SavePathConflictException(savePath);
		}
		registeredConfigMeta.put(configPageId, savePath);
	}
	
	/**
	 * 初始化配置系统
	 * 包含了发布配置相关的注册事件(现在还没有事件系统&.&)和进行首次加载
	 *
	 * <font color="red">可以且只可以在系统初始化阶段执行一次</font>，
	 * iCee 初始化时已经默认执行，请勿再次调用次方法
	 *
	 * @throws ConfigGeneralExceptions 在加载配置文件时遇到的异常的集合
	 */
	public static void init () throws ConfigGeneralExceptions {
		
		// 首次加载的加载状态检测
		if (!(configureTypes.isEmpty() && registeredConfigMeta.isEmpty() && loadedConfig.isEmpty())) {
			Log.logger.warn("It seems that Configure#init is called repeatedly!");
			Log.logger.warn("The management would continue to execute, which is likely to cause duplication of registration.");
		} else if (defaultLanguage != null) {
			Log.logger.info("The Configure#defaultLanguage has already been initialize before the Configure initialization?");
		}
		
		Log.logger.info("Start initialize Configures.");
		
		// 注册基础内容
		Log.logger.debug("Registering core configure.");
		CommonConfigTypes.loadDefaultConfigTypes();
		registerCoreConfigures();
		
		Log.logger.info("[[[TODO]]] Config Registry Events");
		// todo Configure File Register Event
		
		// 首次加载
		load();
		
		Log.logger.info("Done initialize Configures.");
		
	}
	
	/**
	 * 加载/刷新配置文件
	 *
	 * @throws ConfigGeneralExceptions 在加载配置文件时遇到的异常的集合
	 */
	public static void load () throws ConfigGeneralExceptions {
		
		Log.logger.info("Start loading configure...");
		
		/*
		 * 配置文件异常表
		 * 列表中包含循环读取时出现的每一个异常
		 * Pair 的 A 元素为 configPageId，B 元素为异常对象
		 */
		ConfigGeneralExceptions exceptions = new ConfigGeneralExceptions();
		// 创建文件夹
		try {
			File dir = new File(USER_CONFIG_PATH);
			if (!(dir.isDirectory() || dir.mkdir())) {
				exceptions.add(
						new ConfigIOException("Failed to create config dir."),
						"$ROOT"
				);
				return;
			}
		} catch (SecurityException e) {
			exceptions.add(
					new ConfigIOException("Failed to create config dir due to a security exception", e),
					"$ROOT"
			);
			return;
		}
		// 遍历每个注册的配置文件
		registeredConfigMeta.forEach((id, path) -> {
			
			Log.logger.debug("loading config page {}({})", id, path);
			boolean isUserConfigGenerated = false;
			File userConfigFile = new File(USER_CONFIG_PATH, path + CONFIG_FILE_EXTENSION);
			try {
				
				// 获取配置文件的格式配置
				Config meta;
				try {
					meta = ConfigFactory.parseString(
							Resources.getMetaFileAsString(id + "/" + CONFIG_META_FILENAME)
					);
				} catch (IOException|ConfigException.Parse e) {
					exceptions.add(e, id);
					throw TagAsException.INSTANCE;
				}
				boolean isMetaContainsException = false;
				for (int i = 0; i < meta.getConfigList(CONFIG_META_DATA_TAG).size(); i++) {
					// 检查配置文件格式配置是否符合要求
					Config def = meta.getConfigList(CONFIG_META_DATA_TAG).get(i);
					String nodeName;
					try {
						nodeName = def.getString(IConfigType.NODE_TAG);
					} catch (ConfigException.Missing e) {
						isMetaContainsException = true;
						exceptions.add(new ConfigMetaException.WrongValueTypeException(i), id);
						continue;
					} catch (ConfigException.WrongType e) {
						isMetaContainsException = true;
						exceptions.add(new ConfigMetaException.WrongValueTypeException(
								i,
								def.getValue(IConfigType.TYPE_TAG).valueType().name(),
								ConfigValueType.STRING.name()
						), id);
						continue;
					}
					try {
						String tagName = def.getString(IConfigType.TYPE_TAG);
						if (!configureTypes.containsKey(tagName)) {
							isMetaContainsException = true;
							exceptions.add(new ConfigMetaException.NoSuchTypeException(tagName, nodeName), id);
						}
					} catch (ConfigException.Missing e) {
						isMetaContainsException = true;
						exceptions.add(new ConfigMetaException.WrongValueTypeException(nodeName), id);
					} catch (ConfigException.WrongType e) {
						isMetaContainsException = true;
						exceptions.add(new ConfigMetaException.WrongValueTypeException(
								nodeName,
								def.getValue(IConfigType.TYPE_TAG).valueType().name(),
								ConfigValueType.STRING.name()
						), id);
					}
					// 可能会做深度检测
				}
				if (isMetaContainsException) throw TagAsException.INSTANCE;
				Log.logger.debug("succeed load config meta of {}", id);
				
				// 用户配置文件预处理
				Config userConfig;
				try {
					// 获取用户定义的配置文件，或者生成
					if (!userConfigFile.isFile()) {
						generateConfigPage(id, meta, userConfigFile);
						isUserConfigGenerated = true;
					}
					
					userConfig = ConfigFactory.parseString(
							FileHelper.getContentFromStream(new FileInputStream(userConfigFile))
					);
					// 检查版本
					if (
							meta.getInt(CONFIG_META_VERSION_TAG) != userConfig.getInt(CONFIG_META_VERSION_TAG)
					) updateConfigPage(id, meta, userConfigFile, userConfig.getInt(CONFIG_META_VERSION_TAG));
				} catch (ConfigIOException|IOException|ConfigException.Parse e) {
					exceptions.add(e, id);
					throw TagAsException.INSTANCE;
				}
				
				// 用户配置文件检查
				Map<String, IConfigValue> values = new HashMap<>();
				AtomicBoolean isUserConfigContainsExceptions = new AtomicBoolean(false);
				meta.getConfigList(CONFIG_META_DATA_TAG).forEach(metaNode -> {
					try {
						values.put(metaNode.getString(
								IConfigType.NODE_TAG),
								configureTypes.get(
										metaNode.getString(IConfigType.TYPE_TAG)
								).parse(metaNode, userConfig)
						);
					} catch (ConfigTypeException e) {
						exceptions.add(e, id);
						isUserConfigContainsExceptions.set(true);
					}
				});
				if (isUserConfigContainsExceptions.get()) throw TagAsException.INSTANCE;
				loadedConfig.put(id, values);
				
				// 如果读取的是 CORE 配置文件，则更新默认语言
				if (LANG_CONFIG_PAGE_ID.equals(id)) {
					IConfigValue value = values.get(LANG_CONFIG_KEY); // todo?
					if (value instanceof ConfigTypeLanguage.Value) {
						defaultLanguage = ((ConfigTypeLanguage.Value)value).get();
					} else {
						exceptionLanguageUnavailable();
					}
				}
				
				Log.logger.info("Succeed to load config {}({}).", id, path);
				
			} catch (TagAsException tag) {
				Log.logger.warn("Failed to load config {}({}).", id, path);
				if (isUserConfigGenerated && userConfigFile.delete()) Log.logger.info("deleted errored generated file");
			} catch (Exception unknown) {
				Log.logger.warn("Failed to load config {}({}).", id, path);
				if (isUserConfigGenerated && userConfigFile.delete()) Log.logger.info("deleted errored generated file");
				throw unknown;
			}
		});
		
		Log.logger.info("Configures load done.");
		if (!exceptions.isEmpty()) {
			throw exceptions;
		}
		
	}
	
	/**
	 * 生成用户配置文件
	 *
	 * @param configPageId 配置文件ID
	 * @param meta 配置文件元数据
	 * @param userConfigFile 用户配置文件储存位置
	 */
	private static void generateConfigPage (String configPageId, Config meta, File userConfigFile)
	throws ConfigIOException {
		
		Log.logger.info("Generating config page {}({})", configPageId, getConfigPageSavePath(configPageId));
		
		if (defaultLanguage == null) defaultLanguage = I18n.getSystemLanguage();
		final AtomicReference<String> template = new AtomicReference<>();
		
		try {
			// 寻找可用模板
			searchForConfigPageTemplate(configPageId, meta, template);
		} catch (TagAsException ignored) {
			
			//通过找到的模板来生成
			meta.getConfigList(CONFIG_META_DATA_TAG).forEach(
					metaNode -> configureTypes.get(metaNode.getString(IConfigType.TYPE_TAG)).summon(metaNode, template)
			);
			
			// 写配置文件
			outputConfigFile(userConfigFile, template);
			
			return;
			
		}
		
		// 没有找到可用的配置文件模板
		throw new ConfigIOException("No available config template!");
		
	}
	
	/**
	 * 更新用户配置文件
	 *
	 * @param configPageId 配置文件注册ID
	 * @param meta 配置文件元数据
	 * @param userConfigFile 用户配置文件的储存位置
	 * @param oldVersion 用户配置文件当前版本号
	 */
	private static void updateConfigPage (String configPageId, Config meta, File userConfigFile, int oldVersion)
	throws ConfigIOException {
		
		Log.logger.info("Updating config page {}({})", configPageId, getConfigPageSavePath(configPageId));
		
		boolean loadLang = false;
		if (defaultLanguage == null) {
			defaultLanguage = I18n.getSystemLanguage();
			loadLang = true;
		}
		final AtomicReference<String> template = new AtomicReference<>();
		
		try {
			// 寻找可用模板
			searchForConfigPageTemplate(configPageId, meta, template);
		} catch (TagAsException ignored) {
			
			// 读取原文件
			Config oldData = ConfigFactory.empty();
			try {
				oldData = ConfigFactory.parseString(
						FileHelper.getContentFromStream(new FileInputStream(userConfigFile))
				);
			} catch (FileNotFoundException ignored2) { }
			
			//通过找到的模板来生成
			Config finalOldData = oldData;
			meta.getConfigList(CONFIG_META_DATA_TAG).forEach(
					metaNode -> configureTypes.get(metaNode.getString(IConfigType.TYPE_TAG)).update(
							metaNode, template, finalOldData, oldVersion
					)
			);
			
			// 写入新文件
			outputConfigFile(userConfigFile, template);
			
			Config config = ConfigFactory.parseString(template.get());
			// 如果是更新了 CORE 配置文件，则再刷新一遍以更新到正确的语言
			if (loadLang) {
				defaultLanguage = I18n.turnLocalized(
						config.getString(LANG_CONFIG_KEY)
				);
				updateConfigPage(configPageId, meta, userConfigFile, config.getInt(CONFIG_META_VERSION_TAG));
			}
			
			return;
			
		}
		
		// 没有找到可用的配置文件模板
		throw new ConfigIOException("No available config template!");
		
	}
	
	/**
	 * 向配置文件储存位置输出配置文件
	 *
	 * @param userConfigFile 配置文件储存位置
	 * @param template 配置文件模板内容
	 */
	private static void outputConfigFile (File userConfigFile, AtomicReference<String> template)
	throws ConfigIOException {
		FileOutputStream os;
		try {
			if (userConfigFile.createNewFile())
				Log.logger.trace("created new config file {}", userConfigFile);
			os = new FileOutputStream(userConfigFile);
			os.write(template.get().getBytes());
		} catch (IOException e) {
			if (userConfigFile.delete()) {
				Log.logger.info(String.format("Cleared the error configure file %s.", userConfigFile));
			}
			throw new ConfigIOException("Unable to write config template on " + userConfigFile, e);
		}
		Log.logger.trace("succeed");
	}
	
	/**
	 * 从资源文件中寻找可用的配置文件模板
	 *
	 * @param configPageId 配置文件注册ID
	 * @param meta 配置文件的元数据
	 * @param template 模板储存单元
	 */
	private static void searchForConfigPageTemplate (String configPageId, Config meta, AtomicReference<String> template) {
		I18n.forEachFrom(defaultLanguage, lang -> {
			try {
				template.set(FileHelper.getContentFromStream(Resources.getMetaFile(
						String.format("/%s/%s%s", configPageId, lang.getLangTag(), CONFIG_FILE_EXTENSION)
				)));
				if (
						ConfigFactory.parseString(template.get()).getInt(CONFIG_META_VERSION_TAG) !=
						meta.getInt(CONFIG_META_VERSION_TAG)
				) return; // 版本不对应
			} catch (IOException e) {
				return; // 读取文件失败（一般应该是文件不存在）
			}
			throw TagAsException.INSTANCE; // 成功读取，跳出循环
		});
	}
	
	private static void registerCoreConfigures () {
		try {
			registerConfig(CORE_ID, CORE_ID);
			registerConfig(CORE_BOT_ID, CORE_BOT_ID);
		} catch (ConfigManageException.ConfigIdConflictException | ConfigManageException.SavePathConflictException e) {
			Log.logger.fatal("Conflict occurred while registering core configures!", e);
			iCee.exit(18);
		}
	}
	
	private static void exceptionLanguageUnavailable () {
		// TODO?
	}
	
}
