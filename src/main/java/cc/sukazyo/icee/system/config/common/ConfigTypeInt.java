package cc.sukazyo.icee.system.config.common;

import cc.sukazyo.icee.system.config.ConfigTypeException;
import cc.sukazyo.icee.system.config.IConfigType;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;

import java.util.concurrent.atomic.AtomicReference;

public class ConfigTypeInt extends ConfigTypeWithCommonUpdate {
	
	public static final String TYPE_NAME = "int";
	
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
		int value;
		try {
			value = dataSource.getInt(key);
		} catch (ConfigException.Missing e) {
			throw new ConfigTypeException.WrongValueTypeException(key);
		} catch (ConfigException.WrongType e) {
			throw new ConfigTypeException.WrongValueTypeException(key, "int", dataSource.getValue(key).valueType().name());
		}
		if (meta.hasPath(CommonConfigTypes.REQUIRED_TAG)) {
			int minimum = meta.getIntList(CommonConfigTypes.REQUIRED_TAG).get(0);
			int maximum = meta.getIntList(CommonConfigTypes.REQUIRED_TAG).get(1);
			if (
					value <  minimum||
					value >= maximum
			) throw new ConfigTypeException.ValueOutOfRangeException(
					key,
					String.valueOf(value),
					String.valueOf(minimum),
					String.valueOf(maximum)
			);
		}
		return new Value(this, value);
	}
	
	public static class Value implements IConfigValue {
		
		private final IConfigType resolver;
		
		private final int data;
		
		private Value (IConfigType resolver, int data) {
			this.resolver = resolver;
			this.data = data;
		}
		
		@Override
		public IConfigType getType () {
			return resolver;
		}
		
		public int get () {
			return data;
		}
		
	}
	
}
