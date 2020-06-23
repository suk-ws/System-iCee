package cc.sukazyo.icee.mirai;

import cc.sukazyo.icee.system.Proper;
import cc.sukazyo.icee.system.RunState;
import cc.sukazyo.icee.util.Log;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactoryJvm;
import net.mamoe.mirai.event.Events;
import net.mamoe.mirai.utils.BotConfiguration;

public class MiraiQQ extends Thread {
	
	public Bot bot;
	
	private int state = RunState.OFF;
	
	public MiraiQQ() {
		
		// 设置线程名
		this.setName("Mirai QQ");
		
		// 执行配置
		BotConfiguration conf = BotConfiguration.getDefault();
		
		conf.setBotLoggerSupplier(bot1 -> new LoggerMirai());
		conf.setNetworkLoggerSupplier(bot1 -> new LoggerMirai());
		
		// 生成 bot 实例
		bot = BotFactoryJvm.newBot(
				Proper.user.bot.mirai.qqId,
				Proper.user.bot.mirai.password,
				conf
		);
		
		// 启动 Mirai
		if (Proper.user.bot.mirai.apply) {
			start();
		} else {
			Log.logger.info("Mirai QQ doesn't applied");
		}
		
	}
	
	public void run() {
		
		bot.login();
		
		Events.registerEvents(bot, new EventHandle());
		
		bot.getGroup(651637726).sendMessage("HI,iCee!");
		
		state = RunState.RUNNING;
		bot.join();
	}
	
	public void start () {
		if (state > -1) {
			Log.logger.warn("Mirai Bot is running or starting!");
		} else {
			state = RunState.STARTING;
			super.start();
			Log.logger.info("Mirai Bot Called Starting.");
		}
	}
	
	public void stopMirai () {
		if (state < 0) {
			Log.logger.warn("Mirai Bot is already stopped");
		} else {
			stop();
			state = RunState.OFF;
			Log.logger.info("Mirai Bot Stopped.");
		}
	}
	
}
