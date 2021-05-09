package cc.sukazyo.icee.module.bot.mirai;

import cc.sukazyo.icee.common.RunStatus;
import cc.sukazyo.icee.iCee;
import cc.sukazyo.icee.module.bot.IBot;
import cc.sukazyo.icee.system.Conf;
import cc.sukazyo.icee.system.Log;
import cc.sukazyo.icee.system.command.CommandException;
import cc.sukazyo.icee.system.command.CommandManager;
import cc.sukazyo.icee.util.MiraiLogAdapter;
import cc.sukazyo.icee.util.Var;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;

import java.io.File;

public class MiraiBot implements IBot {
	
	Bot bot;
	
	public MiraiBot() throws CommandException.CommandNameConflictException {
		CommandManager.register(new MiraiCommands());
	}
	
	@Override
	public void initialize () {
		
		// 启动 Mirai
		if (Conf.conf.getBoolean("module.bot.mirai.apply")) {
			start();
		} else {
			Log.logger.info("Mirai QQ doesn't applied");
		}
		
	}
	
	private static Bot createBot () {
		
		// 执行配置
		BotConfiguration conf = BotConfiguration.getDefault();
		conf.setBotLoggerSupplier(bot1 -> new MiraiLogAdapter(Log.logger));
		conf.setNetworkLoggerSupplier(bot1 -> new MiraiLogAdapter(Log.logger));
		conf.setProtocol(BotConfiguration.MiraiProtocol.ANDROID_PAD);
		conf.setCacheDir(new File("./data/cache/mirai"));
		conf.enableContactCache();
//		conf.setDeviceInfo(new DeviceInfo(
//          // TODO DeviceInfo Configuration
//		));
		
		// 生成 bot 实例
		return BotFactory.INSTANCE.newBot(
				Conf.conf.getLong("module.bot.mirai.qqid"),
				Conf.conf.getString("module.bot.mirai.password"),
				conf
		);
		
	}
	
	public void start () {
		if (getStatus().canStart()) {
			bot = createBot();
			bot.login();
			bot.getEventChannel().registerListenerHost(EventHandle.INSTANCE);
			Log.logger.info("Mirai Bot Called Starting.");
		} else {
			Log.logger.warn("Mirai Bot is already running or starting!");
		}
	}
	
	public void stop () {
		if (getStatus().canStop()) {
			bot.closeAndJoin(null);
			bot = null;
			Log.logger.info("Mirai Bot Stopped.");
		} else {
			Log.logger.warn("Mirai Bot is already stopped");
		}
	}
	
	@Override
	public RunStatus getStatus() {
		if (bot == null) {
			return RunStatus.OFF;
		} else if (bot.isOnline()) {
			return RunStatus.ON;
		} else {
			return RunStatus.WAITING;
		}
	}
	
	@Override
	public String getRegistryName () {
		return "qq_mirai";
	}
	
	@Override
	public String getVersion () {
		return iCee.VERSION;
	}
	
	@Override
	public String getDisplayVersion () {
		return Var.ICEE_VERSION_DISPLAY.value;
	}
	
}
