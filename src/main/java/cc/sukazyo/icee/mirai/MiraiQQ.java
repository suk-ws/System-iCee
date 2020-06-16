package cc.sukazyo.icee.mirai;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactoryJvm;
import net.mamoe.mirai.event.Events;

public class MiraiQQ {
	
	public Bot bot;
	
	
	public MiraiQQ() {
		bot = BotFactoryJvm.newBot(1467664001L, "Password");
		
		bot.login();
		
		Events.registerEvents(bot, new EventHandle());
		
		bot.join();
	}
	
}
