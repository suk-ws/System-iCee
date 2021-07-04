package cc.sukazyo.icee.system.config.common;

import cc.sukazyo.icee.system.I18n;
import cc.sukazyo.icee.system.config.ConfigTypeException;
import cc.sukazyo.icee.system.config.IConfigType;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;

import java.util.concurrent.atomic.AtomicReference;

public class ConfigTypeLanguage extends ConfigTypeWithCommonUpdate {
	
	public static final String TYPE_NAME = "language";
	
	@Override
	public String getTypeName () {
		return TYPE_NAME;
	}
	
	public static final String SYSTEM_LANGUAGE_TAG = "<<SYSTEM_LANG>>";
	
	@Override
	public void summon (Config meta, AtomicReference<String> template) {
		String def = meta.getString(DEFAULT_TAG);
		template.set(template.get().replace(
				String.format("<<%s>>", meta.getString(NODE_TAG)),
				SYSTEM_LANGUAGE_TAG.equals(def)?I18n.getSystemLanguage().getLangTag():def
		));
	}
	
	@Override
	public Value parse (Config meta, Config dataSource) throws ConfigTypeException {
		String key = meta.getString(NODE_TAG);
		String value;
		try {
			value = dataSource.getString(key);
		} catch (ConfigException.Missing e) {
			throw new ConfigTypeException.WrongValueTypeException(key);
		} catch (ConfigException.WrongType e) {
			throw new ConfigTypeException.WrongValueTypeException(key, "language-tag(string)", dataSource.getValue(key).valueType().name());
		}
		return new Value(this, value);
	}
	
	public static class Value implements IConfigValue {
		
		private final IConfigType resolver;
		
		private final String data;
		
		private Value (IConfigType resolver, String data) {
			this.resolver = resolver;
			this.data = data;
		}
		
		@Override
		public IConfigType getType () {
			return resolver;
		}
		
		@SuppressWarnings("unused")
		public String getOriginalTag () {
			return data;
		}
		
		public I18n.Localized get () {
			return SYSTEM_LANGUAGE_TAG.equals(data)?I18n.getSystemLanguage():I18n.turnLocalized(data);
		}
		
	}
	
}
