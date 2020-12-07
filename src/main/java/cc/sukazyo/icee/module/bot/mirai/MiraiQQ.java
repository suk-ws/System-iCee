package cc.sukazyo.icee.module.bot.mirai;

import cc.sukazyo.icee.common.RunStatus;
import cc.sukazyo.icee.module.bot.IBot;
import cc.sukazyo.icee.system.Conf;
import cc.sukazyo.icee.system.Log;
import kotlinx.coroutines.Job;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactoryJvm;
import net.mamoe.mirai.event.Events;
import net.mamoe.mirai.utils.BotConfiguration;
import net.mamoe.mirai.utils.LoggerAdapters;

import java.util.Objects;

public class MiraiQQ implements IBot {
	
	private final Runner RUNNER = new Runner();
	private Thread thread;
	
	private static class Runner implements Runnable {
		final String THREAD_NAME = "Mirai QQ Docker";
		Bot bot;
		@Override
		public void run() {
			bot.login();
			Events.registerEvents(bot, new EventHandle());
			bot.getFriend(863731218).sendMessage("HI,iCee!");
		}
	}
	
	public MiraiQQ() {
		
		// 启动 Mirai
		if (Conf.conf.getBoolean("module.bot.mirai.apply")) {
			start();
		} else {
			Log.logger.info("Mirai QQ doesn't applied");
		}
		
	}
	
	private Bot createBot () {
		
		// 执行配置
		BotConfiguration conf = BotConfiguration.getDefault();
		conf.setBotLoggerSupplier(bot1 -> LoggerAdapters.asMiraiLogger(Log.logger));
		conf.setNetworkLoggerSupplier(bot1 -> LoggerAdapters.asMiraiLogger(Log.logger));
		
		// 生成 bot 实例
		return BotFactoryJvm.newBot(
				Conf.conf.getLong("module.bot.mirai.qqid"),
				Conf.conf.getString("module.bot.mirai.password"),
				conf
		);
		
	}
	
	@Override
	public void start () {
		if (getStatus().canStart()) {
			RUNNER.bot = createBot();
			thread = new Thread(RUNNER, RUNNER.THREAD_NAME);
			thread.start();
			Log.logger.info("Mirai Bot Called Starting.");
		} else {
			Log.logger.warn("Mirai Bot is already running or starting!");
		}
	}
	
	@Override
	public void stop () {
		if (getStatus().canStop()) {
			RUNNER.bot.close(null);
			Log.logger.info("Mirai Bot Stopped.");
		} else {
			Log.logger.warn("Mirai Bot is already stopped");
		}
	}
	
	@Override
	public RunStatus getStatus() {
		if (thread == null) {
			return RunStatus.OFF;
		} else if (thread.getState()!= Thread.State.TERMINATED) {
			return RunStatus.STARTING;
		} else if (Objects.requireNonNull(RUNNER.bot.getCoroutineContext().get(Job.Key)).isActive()) {
			return RunStatus.ON;
		} else {
			return RunStatus.OFF;
		}
	}
	
}