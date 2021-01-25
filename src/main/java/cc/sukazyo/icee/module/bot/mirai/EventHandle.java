package cc.sukazyo.icee.module.bot.mirai;

import cc.sukazyo.icee.module.bot.CommonBotMessage;
import cc.sukazyo.icee.system.Log;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.BotOfflineEvent;
import net.mamoe.mirai.event.events.BotOnlineEvent;
import net.mamoe.mirai.event.events.BotReloginEvent;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;

public class EventHandle extends SimpleListenerHost {
	
	public static final EventHandle INSTANCE = new EventHandle();
	
	@EventHandler
	public void onGroupMessage(GroupMessageEvent event) {
		if (event.getGroup().getId() == 651637726L) {
			new CommonBotMessage(event).doReply();
		}
	}
	
	@EventHandler
	public void onFriendMessage(FriendMessageEvent event) {
		new CommonBotMessage(event).doReply();
	}
	
	@EventHandler
	public void onOnline(BotOnlineEvent event) {
		Log.logger.info("确认 Mirai Bot 上线。");
	}
	
	@EventHandler
	public void onOffline(BotOfflineEvent event) {
		Log.logger.info("Mirai Bot 已下线。");
	}
	
	@EventHandler
	public void onRelogin(BotReloginEvent event) {
		Log.logger.info("Mirai Bot 尝试重新登陆中...");
	}
	
}
