package cc.sukazyo.icee.system;

public class ProperTemplate {
	
	public static class ConfTemplateSystem {
		
		int format = 2;
		
		String lang;
		
		public static class ConfTemplateHttp {
			
			public boolean apply;
			
			public int port;
			
		} public ConfTemplateHttp http;
		
		public static class ConfTemplateBot {
			
			public static class ConfTemplateDiscord {
				
				public boolean apply;
				public String token;
				
			} public ConfTemplateDiscord discord;
			
			public static class ConfTemplateMirai {
				
				public boolean apply;
				public long qqId;
				public String password;
				
			} public ConfTemplateMirai mirai;
			
		} public ConfTemplateBot bot;
		
	}
	
	public static class ConfTemplateBot {
		
		public int format = 1;
		
		public static class ConfTemplateCall {
			
			public String mode;
			public String[] raw;
			public String[] text;
			
		} public ConfTemplateCall call;
		
	}
	
}
