package cc.sukazyo.icee.module.http;

import cc.sukazyo.icee.common.RunStatus;
import cc.sukazyo.icee.module.IModule;
import cc.sukazyo.icee.system.Log;
import cc.sukazyo.icee.system.Conf;
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
	
	@Override
	public void start () {
		// TODO function
	}
	
	@Override
	public void stop () {
		// TODO function
	}
	
	@Override
	public RunStatus getStatus () {
		return null;// TODO function
	}
	
}
