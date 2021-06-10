package cc.sukazyo.icee.system.config.common;

import cc.sukazyo.icee.system.config.ConfigTypeException;
import cc.sukazyo.icee.system.config.IConfigType;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;

import java.util.concurrent.atomic.AtomicReference;

public class ConfigTypeBoolean extends ConfigTypeWithCommonUpdate {
	
	public static final String TYPE_NAME = "boolean";
	
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
		boolean value;
		try {
			value = dataSource.getBoolean(key);
		} catch (ConfigException.Missing e) {
			throw new ConfigTypeException.WrongValueTypeException(key);
		} catch (ConfigException.WrongType e) {
			throw new ConfigTypeException.WrongValueTypeException(key, "boolean", dataSource.getValue(key).valueType().name());
		}
		return new Value(this, value);
	}
	
	public static class Value implements IConfigValue {
		
		private final boolean data;
		
		private final IConfigType resolver;
		
		private Value (IConfigType resolver, boolean data) {
			this.resolver = resolver;
			this.data = data;
		}
		
		@Override
		public IConfigType getType () {
			return resolver;
		}
		
		public boolean get () {
			return data;
		}
		
	}
	
}
