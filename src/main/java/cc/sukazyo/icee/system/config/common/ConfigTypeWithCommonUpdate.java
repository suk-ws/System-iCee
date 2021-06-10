package cc.sukazyo.icee.system.config.common;

import cc.sukazyo.icee.system.config.IConfigType;
import cc.sukazyo.icee.util.TagAsException;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public abstract class ConfigTypeWithCommonUpdate implements IConfigType {
	
	@Override
	public void update (Config meta, AtomicReference<String> template, Config oldData, int oldVersion) {
		
		// 获取旧版本的配置定义位置
		AtomicReference<String> nodePath = new AtomicReference<>(meta.getString(NODE_TAG));
		try {
			if (meta.hasPath(CommonConfigTypes.OLD_VERSIONS_TAG)) {
				meta.getConfigList(CommonConfigTypes.OLD_VERSIONS_TAG).forEach(oldNext -> {
					if (oldVersion > oldNext.getInt(CommonConfigTypes.OLD_VERSIONS_VER_TAG)) {
						throw TagAsException.INSTANCE;
					} else {
						nodePath.set(oldNext.getString(CommonConfigTypes.OLD_VERSIONS_NODE_TAG));
					}
				});
			}
		} catch (TagAsException ignored) {}
		
		// 从旧版本更新，失败则重新生成
		if (oldData.hasPath(nodePath.get())) {
			HashMap<String, ConfigValue> newMeta = new HashMap<>();
			meta.entrySet().forEach(entry -> {
				if (!DEFAULT_TAG.equals(entry.getKey())) {
					newMeta.put(entry.getKey(), entry.getValue());
				}
			});
			newMeta.put(DEFAULT_TAG, oldData.getValue(nodePath.get()));
			meta = ConfigFactory.parseMap(newMeta);
		}
		summon(meta, template);
		
	}
	
}
