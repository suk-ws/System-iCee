package cc.sukazyo.icee.system.config.common;

import cc.sukazyo.icee.system.config.ConfigTypeException;
import cc.sukazyo.icee.system.config.IConfigType;
import cc.sukazyo.icee.util.SimpleUtils;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ConfigTypeStringList extends ConfigTypeWithCommonUpdate {
	
	public static final String TYPE_NAME = "string-list";
	
	@Override
	public String getTypeName () {
		return TYPE_NAME;
	}
	
	@Override
	public void summon (Config meta, AtomicReference<String> template) {
		template.set(template.get().replace(
				String.format("<<%s>>", meta.getString(NODE_TAG)),
				SimpleUtils.generateListIndented(
						meta.getStringList(DEFAULT_TAG), "[", "]", " ",
						"\"", "\"",
						",", ", ",
						0, false, 0,
						false, false,
						true, Integer.MAX_VALUE, Integer.MAX_VALUE,
						false, Integer.MAX_VALUE, false
				)
		));
	}
	
	@Override
	public Value parse (Config meta, Config dataSource) throws ConfigTypeException {
		String key = meta.getString(NODE_TAG);
		List<String> value;
		try {
			value = dataSource.getStringList(key);
		} catch (ConfigException.Missing e) {
			throw new ConfigTypeException.WrongValueTypeException(key);
		} catch (ConfigException.WrongType e) {
			throw new ConfigTypeException.WrongValueTypeException(key, "string-list", dataSource.getValue(key).valueType().name());
		}
		return new Value(this, value);
	}
	
	public static class Value implements IConfigValue {
		
		private final IConfigType resolver;
		
		private final List<String> data;
		
		private Value (IConfigType resolver, List<String> data) {
			this.resolver = resolver;
			this.data = data;
		}
		
		@Override
		public IConfigType getType () {
			return resolver;
		}
		
		public List<String> get () {
			return data;
		}
		
	}
	
}
