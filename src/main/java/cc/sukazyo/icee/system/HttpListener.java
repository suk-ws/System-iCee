package cc.sukazyo.icee.system;

import cc.sukazyo.icee.util.http.HttpServer;
import cc.sukazyo.icee.util.http.Record;
import cc.sukazyo.icee.util.http.RecordHandler;

public class HttpListener implements RecordHandler {
	
	HttpServer server;
	
	public HttpListener() {
		
		server = new HttpServer(Proper.system.http.port);
		server.addRecordHandler(this);
		if (Proper.system.http.apply) {
			server.start();
			Log.logger.info("Start Http Server");
		} else {
			Log.logger.info("Http Server doesn't applied.");
		}
	
	}
	
	@Override
	public void handleRecord(Record record) {
		record.setRecord(record.getMethod() + "<br />" + record.getUri() + "<br />" + record.getVersion());
	}
}
