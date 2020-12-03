package cc.sukazyo.icee.bot.mirai;

import cc.sukazyo.icee.module.RunState;
import cc.sukazyo.icee.module.i.IBot;
import cc.sukazyo.icee.system.Conf;
import cc.sukazyo.icee.system.Log;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactoryJvm;
import net.mamoe.mirai.event.Events;
import net.mamoe.mirai.utils.BotConfiguration;

public class MiraiQQ implements IBot {
	
	private final Runner RUNNER = new Runner();
	private static class Runner extends Thread {
		Bot bot;
		RunState state = RunState.OFF;
		@Override
		public void run() {
			bot.login();
			Events.registerEvents(bot, new EventHandle());
			bot.getGroup(651637726).sendMessage("HI,iCee!");
			state = RunState.RUNNING;
			bot.join();
		}
	}
	
	public MiraiQQ() {
		
		// 设置线程名
		RUNNER.setName("Mirai QQ");
		
		// 执行配置
		BotConfiguration conf = BotConfiguration.getDefault();
		conf.setBotLoggerSupplier(bot1 -> new LoggerMirai());
		conf.setNetworkLoggerSupplier(bot1 -> new LoggerMirai());
		
		// 生成 bot 实例
		RUNNER.bot = BotFactoryJvm.newBot(
				Conf.conf.getLong("module.bot.mirai.qqid"),
				Conf.conf.getString("module.bot.mirai.password"),
				conf
		);
		
		// 启动 Mirai
		if (Conf.conf.getBoolean("module.bot.mirai.apply")) {
			start();
		} else {
			Log.logger.info("Mirai QQ doesn't applied");
		}
		
	}
	
	@Override
	public void start () {
		if (RUNNER.state.canStart()) {
			RUNNER.state = RunState.STARTING;
			RUNNER.start();
			Log.logger.info("Mirai Bot Called Starting.");
		} else {
			Log.logger.warn("Mirai Bot is already running or starting!");
		}
	}
	
	@Override
	public void stop () {
		if (RUNNER.state.canStop()) {
			Log.logger.warn("Mirai Bot is already stopped");
		} else {
			RUNNER.interrupt();
			RUNNER.state = RunState.OFF;
			Log.logger.info("Mirai Bot Stopped.");
		}
	}
	
	@Override
	public RunState getState () {
		return RUNNER.state;
	}
	
}
