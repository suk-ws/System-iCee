package cc.sukazyo.icee.bot.mirai;

import cc.sukazyo.icee.system.Log;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.event.events.BotOfflineEvent;
import net.mamoe.mirai.event.events.BotOnlineEvent;
import net.mamoe.mirai.event.events.MessageSendEvent;
import net.mamoe.mirai.message.GroupMessageEvent;

public class EventHandle implements ListenerHost {
	
	@EventHandler
	public void onGroupMessage(GroupMessageEvent event) {
		String msg = event.getMessage().toString();
		if (event.getGroup().getId() == 651637726L) {
			Log.logger.debug("iCee QQ Get: " + msg);
		}
	}
	
	@EventHandler
	public void onOnline(BotOnlineEvent event) {
		event.getBot().getGroup(651637726L).sendMessage("iCee 上线啦！");
		Log.logger.info("确认 Mirai Bot 上线。");
	}
	
	@EventHandler
	public void onOffline(BotOfflineEvent event) {
		event.getBot().getGroup(651637726L).sendMessage("iCee 下线啦！");
		Log.logger.info("Mirai Bot 已下线。");
	}
	
	@EventHandler
	public void onSelfSend(MessageSendEvent event) {
		Log.logger.debug("iCee QQ Send: ");
	}
	
}
