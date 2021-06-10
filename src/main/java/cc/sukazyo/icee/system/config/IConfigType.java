package cc.sukazyo.icee.system.config;

import com.typesafe.config.Config;

import java.util.concurrent.atomic.AtomicReference;

public interface IConfigType {
	
	String NODE_TAG = "node";
	String TYPE_TAG = "type";
	String DEFAULT_TAG = "default";
	
	String getTypeName();
	
	void summon (Config meta, AtomicReference<String> template);
	
	IConfigValue parse (Config meta, Config dataSource) throws ConfigTypeException;
	
	void update (Config meta, AtomicReference<String> template, Config oldData, int oldVersion);
	
	interface IConfigValue {
		
		IConfigType getType ();
		
	}
	
}
