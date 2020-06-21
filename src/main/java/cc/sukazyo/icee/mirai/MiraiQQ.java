package cc.sukazyo.icee.mirai;

import cc.sukazyo.icee.system.Proper;
import cc.sukazyo.icee.util.Log;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactoryJvm;
import net.mamoe.mirai.event.Events;
import net.mamoe.mirai.utils.BotConfiguration;

public class MiraiQQ extends Thread {
	
	public Bot bot;
	
	
	public MiraiQQ() { this.setName("Mirai Start"); }
	
	public void run() {
		
		if (!Proper.user.bot.mirai.apply) {
			Log.logger.info("Mirai QQ doesn't applied");
			return;
		}
		
		BotConfiguration conf = BotConfiguration.getDefault();
		
		conf.setBotLoggerSupplier(bot1 -> new LoggerMirai());
		conf.setNetworkLoggerSupplier(bot1 -> new LoggerMirai());
		
		bot = BotFactoryJvm.newBot(
				Proper.user.bot.mirai.qqId,
				Proper.user.bot.mirai.password,
				conf
		);
		
		bot.login();
		
		Events.registerEvents(bot, new EventHandle());
		
		bot.getGroup(651637726).sendMessage("HI,iCee!");
		
		bot.join();
	}
	
}
