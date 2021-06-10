package cc.sukazyo.icee.system.config.common;

import cc.sukazyo.icee.system.config.ConfigTypeException;
import cc.sukazyo.icee.system.config.IConfigType;
import cc.sukazyo.icee.util.SimpleUtils;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;

import java.util.concurrent.atomic.AtomicReference;

public class ConfigTypeLong extends ConfigTypeWithCommonUpdate {
	
	public static final String TYPE_NAME = "long";
	
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
		long value;
		try {
			value = dataSource.getLong(key);
		} catch (ConfigException.Missing e) {
			throw new ConfigTypeException.WrongValueTypeException(key);
		} catch (ConfigException.WrongType e) {
			throw new ConfigTypeException.WrongValueTypeException(key, "long", dataSource.getValue(key).valueType().name());
		}
		if (meta.hasPath(CommonConfigTypes.REQUIRED_TAG)) {
			long minimum = meta.getLongList(CommonConfigTypes.REQUIRED_TAG).get(0);
			long maximum = meta.getLongList(CommonConfigTypes.REQUIRED_TAG).get(1);
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
		
		private final long data;
		
		private Value (IConfigType resolver, long data) {
			this.resolver = resolver;
			this.data = data;
		}
		
		@Override
		public IConfigType getType () {
			return resolver;
		}
		
		public long get () {
			return data;
		}
		
	}
	
}
