package cc.sukazyo.icee.bot.mirai;

import cc.sukazyo.icee.module.RunState;
import cc.sukazyo.icee.module.i.IBot;
import cc.sukazyo.icee.system.Conf;
import cc.sukazyo.icee.system.Log;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactoryJvm;
import net.mamoe.mirai.event.Events;
import net.mamoe.mirai.utils.BotConfiguration;
import net.mamoe.mirai.utils.LoggerAdapters;

public class MiraiQQ implements IBot {
	
	private final Runner RUNNER = new Runner();
	
	private static class Runner implements Runnable {
		final String THREAD_NAME = "Mirai QQ Docker";
		Bot bot;
		RunState state = RunState.OFF;
		@Override
		public void run() {
			bot.login();
			Events.registerEvents(bot, new EventHandle());
			bot.getFriend(863731218).sendMessage("HI,iCee!");
			state = RunState.RUNNING;
		}
	}
	
	public MiraiQQ() {
		
		// 执行配置
		BotConfiguration conf = BotConfiguration.getDefault();
		conf.setBotLoggerSupplier(bot1 -> LoggerAdapters.asMiraiLogger(Log.logger));
		conf.setNetworkLoggerSupplier(bot1 -> LoggerAdapters.asMiraiLogger(Log.logger));
		
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
			new Thread(RUNNER, RUNNER.THREAD_NAME).start();
			Log.logger.info("Mirai Bot Called Starting.");
		} else {
			Log.logger.warn("Mirai Bot is already running or starting!");
		}
	}
	
	@Override
	public void stop () {
		if (RUNNER.state.canStop()) {
			RUNNER.bot.close(new RuntimeException("Bot Should Been Shutdown"));
			RUNNER.state = RunState.OFF;
			Log.logger.info("Mirai Bot Stopped.");
		} else {
			Log.logger.warn("Mirai Bot is already stopped");
		}
	}
	
	@Override
	public RunState getState () {
		return RUNNER.state;
	}
	
}
