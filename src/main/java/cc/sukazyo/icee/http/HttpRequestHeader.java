package cc.sukazyo.icee.http;

import com.sun.net.httpserver.HttpExchange;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestHeader {
	
	public String method;
	public String userAgent;
	
	public String host;
	public String path;
	public Map<String, String> args = new HashMap<>();
	
	public Map<String, String> cookies = new HashMap<>();
	
	public HttpRequestHeader (HttpExchange httpExchange) {
		// 写入请求方式
		method = httpExchange.getRequestMethod();
		userAgent = httpExchange.getRequestHeaders().getFirst("User-agent");
		// 请求路径和请求参数
		host = httpExchange.getRequestURI().getHost();
		path = httpExchange.getRequestURI().getPath();
		if (httpExchange.getRequestMethod().equals("GET") && !httpExchange.getRequestURI().getQuery().equals("") && httpExchange.getRequestURI().getQuery() != null) {
			for (String arg : httpExchange.getRequestURI().getQuery().split("&")) {
				String[] pre = arg.split("=", 2);
				args.put(pre[0], pre[1]);
			}
		}
		// 处理请求cookie
		if (!httpExchange.getRequestHeaders().getFirst("Cookie").equals("") && httpExchange.getRequestHeaders().getFirst("Cookie") != null) {
			for (String row : httpExchange.getRequestHeaders().getFirst("Cookie").split("; ")) {
				String[] pre = row.split("=", 2);
				cookies.put(pre[0], pre[1]);
			}
		}
	}
	
}
