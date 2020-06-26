package cc.sukazyo.icee.util.http;


import cc.sukazyo.icee.system.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务器监听对象，对某个端口进行监听，基于线程的实现
 *
 * 感谢 Kevin 的代码
 * @author Kevin
 * @link https://www.cnblogs.com/csguo/p/7542374.html
 */
public class HttpServer implements Runnable {
	
	/** 服务器监听 */
	private ServerSocket serverSocket;
	
	/** 标志位，表示当前服务器是否正在运行 */
	private boolean isRunning;
	
	/** 观察者 */
	private final List<RecordHandler> recordHandlers = new ArrayList<>();
	
	public HttpServer(int port) {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void stop() {
		this.isRunning = false;
	}
	
	public void start() {
		this.isRunning = true;
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		while (isRunning) {//一直监听，直到受到停止的命令
			Socket socket;
			try {
				socket = serverSocket.accept();//如果没有请求，会一直hold在这里等待，有客户端请求的时候才会继续往下执行
				// log
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));//获取输入流(请求)
				StringBuilder stringBuilder = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null
						&& !line.equals("")) {//得到请求的内容，注意这里作两个判断非空和""都要，只判断null会有问题
					stringBuilder.append(line).append("/n");
				}
				Record record = new Record();
				String[] strs = stringBuilder.toString().split(" ");
				assert strs.length == 3;
				record.setMethod(strs[0]);
				record.setUri(strs[1]);
				record.setVersion(strs[2]);
				record.setVisitDate(new Date(System.currentTimeMillis()));
				notifyRecordHandlers(record);//通知日志记录者对日志作操作
				// echo
				PrintWriter printWriter = new PrintWriter(
						socket.getOutputStream(), true);//这里第二个参数表示自动刷新缓存
				doEcho(printWriter, record);//将日志输出到浏览器
				// release
				printWriter.close();
				bufferedReader.close();
				socket.close();
			} catch (IOException e) {
				Log.logger.error("Http Server meet an exception while reading http request", e);
			}
		}
	}
	
	/**
	 * 将得到的信写回客户端
	 *
	 * @param printWriter printWriter
	 * @param record record
	 */
	private void doEcho(PrintWriter printWriter, Record record) {
		printWriter.write(record.getRecord());
	}
	
	/**
	 * 通知已经注册的监听者做处理
	 *
	 * @param record Record 实例
	 */
	private void notifyRecordHandlers(Record record) {
		for (RecordHandler recordHandler : this.recordHandlers) {
			recordHandler.handleRecord(record);
		}
	}
	
	/**
	 * 添加一个监听器
	 *
	 * @param recordHandler 要添加的监听器
	 */
	public void addRecordHandler(RecordHandler recordHandler) {
		this.recordHandlers.add(recordHandler);
	}
}
