package cc.sukazyo.icee.http;

import cc.sukazyo.icee.system.Log;
import cc.sukazyo.icee.system.Proper;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpListener {
	
	HttpServer server;
	
	public HttpListener() {
		
		try {
			server = HttpServer.create(new InetSocketAddress(Proper.system.http.port), 0);
//			server.createContext("/mtppp/", new HttpMtpppReq()); // 暂时无用
			if (Proper.system.http.apply) {
				server.start();
				Log.logger.info("Start Http Server");
			} else {
				Log.logger.info("Http Server doesn't applied.");
			}
		} catch (IOException e) {
			Log.logger.fatal("Create Http Server Failed", e);
		}
	
	}
	
}
