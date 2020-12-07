package cc.sukazyo.icee.module.bot;

import cc.sukazyo.icee.system.Lang;
import cc.sukazyo.icee.system.Variable;

public class CommandReturn {
	
	public static String command (String[] comm, CommonBotMessage message) {
		
		String returnMsg = "";
		
		switch (comm[0]) {
			
			// 获取到调试命令
			case "debug":
				
				// 回复信息调用头
				StringBuilder local = new StringBuilder("command.debug");
				
				// 子命令 append
				switch (comm[1]) {
					case "user":
						local.append(".user");
						break;
					case "guild":
						local.append(".guild");
						break;
					case "channel":
						local.append(".channel");
						break;
					case "group":
						local.append(".group");
						break;
					default:
						local = new StringBuilder("command.unknown.child");
				}
				
				if (local.toString().equals("command.unknown.child")) {
					// 处理子命令错误
					returnMsg = Variable.compile(Lang.get("command.unknown.child"), comm, 1);
				} else {
					// bot 类型 append
					local.append(typeAppend(message.type));
					returnMsg = Lang.get(local.toString());
				}
				break;
				
			// 找不到命令
			default:
				returnMsg = Variable.compile(Lang.get("command.unknown"), comm, 0);
		}
		
		return returnMsg;
		
	}
	
	private static String typeAppend(CommonBotMessage.Type type) {
		switch (type) {
			case DISCORD:
				return ".discord";
			case MIRAI_GROUP:
				return ".mirai-group";
			case MIRAI_FRIEND:
				return ".mirai-friend";
			default:
				return "";
		}
	}
	
}
