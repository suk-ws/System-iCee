package cc.sukazyo.icee.system;

public class ProperTemplate {
	
	public static class ConfTemplateSystem {
		
		int format = 1;
		
		String lang;
		
		public static class ConfTemplateLog {
			
			String level;
			boolean debugsave;
			
		} ConfTemplateLog log;
		
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
