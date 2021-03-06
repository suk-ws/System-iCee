package cc.sukazyo.icee.module.http;

import cc.sukazyo.icee.common.RunStatus;
import cc.sukazyo.icee.iCee;
import cc.sukazyo.icee.system.module.IModule;
import cc.sukazyo.icee.system.Log;
import cc.sukazyo.icee.system.Conf;
import cc.sukazyo.icee.util.Var;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpListener implements IModule {
	
	HttpServer server;
	
	public HttpListener() { }
	
	@Override
	public void initialize() {
		
		try {
			server = HttpServer.create(new InetSocketAddress(Conf.conf.getInt("module.http.port")), 0);
//			server.createContext("/mtppp/", new HttpMtpppReq()); // 暂时无用
			if (Conf.conf.getBoolean("module.http.apply")) {
				server.start();
				Log.logger.info("Start Http Server");
			} else {
				Log.logger.info("Http Server doesn't applied.");
			}
		} catch (IOException e) {
			Log.logger.fatal("Create Http Server Failed", e);
		}
	
	}
	
	public void start () {
		// TODO function
	}
	
	public void stop () {
		// TODO function
	}
	
	@Override
	public RunStatus getStatus () {
		return null;// TODO function
	}
	
	@Override
	public String getRegistryName () {
		return "http";
	}
	
	@Override
	public String getVersion () {
		return iCee.VERSION;
	}
	
	@Override
	public String getDisplayVersion () {
		return Var.ICEE_VERSION_DISPLAY.value;
	}
	
}
