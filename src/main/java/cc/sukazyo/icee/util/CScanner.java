package cc.sukazyo.icee.util;

import cc.sukazyo.icee.iCee;
import cc.sukazyo.icee.system.Log;
import cc.sukazyo.icee.system.Conf;
import net.dv8tion.jda.api.entities.Activity;

import java.util.Scanner;

public class CScanner extends Thread {
	
	private final Scanner scanner = new Scanner(System.in);
	public String[] command;
	
	public CScanner () { this.setName("SysConsole"); }
	
	@Override
	public void run() {
		
		while (true) {
			
			String tmp = scanner.nextLine();
			command = CommandHelper.format(tmp);
			if (command.length == 0)
				continue;
			Log.logger.info("Console execute command : " + tmp );
			switch (command[0]) {
				case "stop":
					Log.logger.info("Stopping System.");
					System.exit(0);
				case "discord":
					switch (command[1]) {
						case "start":
							iCee.discord.start();
							break;
						case "state":
							Log.logger.info("Doscord Bot Now State : " + iCee.discord.getStatus());
							break;
						case "stop":
						case "exit":
						case "quit":
							iCee.discord.stop();
							break;
						case "activity":
							switch (command[2]) {
								case "playing":
									iCee.discord.setBotActivity(Activity.ActivityType.DEFAULT, command[3]);
									Log.logger.info("Activity set success! The Value will be apply next start.");
									break;
								case "listening":
									iCee.discord.setBotActivity(Activity.ActivityType.LISTENING, command[3]);
									Log.logger.info("Activity set success! The Value will be apply next start.");
									break;
								case "watching":
									iCee.discord.setBotActivity(Activity.ActivityType.WATCHING, command[3]);
									Log.logger.info("Activity set success! The Value will be apply next start.");
									break;
								case "streaming":
									iCee.discord.setBotActivity(Activity.ActivityType.STREAMING, command[3]);
									break;
								default:
									Log.logger.warn("No Activity " + command[2] + ", please check your spell.");
							}
							break;
						case "send":
							iCee.discord.sendDebug(706505304489197568L);
							break;
						default:
							Log.logger.warn("No option <" + command[1] + "> exist!");
					}
					break;
				case "mirai":
				case "qq":
					switch (command[1]) {
						case "start":
							iCee.mirai.start();
							break;
						case "state":
							Log.logger.info("QQ Bot Mirai Now State : " + iCee.mirai.getStatus());
							break;
						case "stop":
							iCee.mirai.stop();
							break;
						default:
							Log.logger.warn("No option <" + command[1] + "> exist!");
					}
					break;
				case "flush":
					Conf.load();
					break;
				default:
					Log.logger.warn("Command <" + command[0] + "> not found.");
			}
			
		}
	}
}
