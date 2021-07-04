package cc.sukazyo.icee.system.config.common;

import cc.sukazyo.icee.system.config.ConfigTypeException;
import cc.sukazyo.icee.system.config.IConfigType;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ConfigTypeString extends ConfigTypeWithCommonUpdate {
	
	public static final String TYPE_NAME = "string";
	
	@Override
	public String getTypeName () {
		return TYPE_NAME;
	}
	
	@Override
	public void summon (Config meta, AtomicReference<String> template) {
		template.set(template.get().replace(
				String.format("<<%s>>", meta.getString(NODE_TAG)),
				meta.getString(DEFAULT_TAG)
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
			throw new ConfigTypeException.WrongValueTypeException(key, "string", dataSource.getValue(key).valueType().name());
		}
		if (meta.hasPath(CommonConfigTypes.REQUIRED_TAG)) {
			AtomicBoolean isOK = new AtomicBoolean(false);
			meta.getStringList(CommonConfigTypes.REQUIRED_TAG).forEach(regex -> {
				if (value.matches(regex)) isOK.set(true);
			});
			if (!isOK.get()) {
				throw new ConfigTypeException.ValueOutOfRangeException(
						key, value,
						meta.getStringList(CommonConfigTypes.REQUIRED_TAG).toString()
				);
			}
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
		
		public String get () {
			return data;
		}
		
	}
	
}
