package cc.sukazyo.icee.system;

public class ProperTemplate {
	
	int format = 1;
	
	String lang;
	
	public static class Log {
		String level;
		boolean debugsave;
	} Log log;
	
	public static class Bot {
		
		public static class Discord {
			public boolean apply;
			public String token;
		} public Discord discord;
		
		public static class Mirai {
			public boolean apply;
			public long qqId;
			public String password;
		} public Mirai mirai;
		
	} public Bot bot;
	
}
