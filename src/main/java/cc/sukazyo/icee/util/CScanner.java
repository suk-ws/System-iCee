package cc.sukazyo.icee.util;

import cc.sukazyo.icee.iCee;

import java.util.Scanner;

public class CScanner extends Thread {
	
	private final Scanner scanner = new Scanner(System.in);
	public String[] command;
	
	public CScanner () { this.setName("SysConsole"); }
	
	@Override
	public void run() {
		
		while (true) {
			
			String tmp = scanner.nextLine();
			Log.logger.info("Console execute command : " + tmp );
			command = CommandHelper.format(tmp);
			
			switch (command[0]) {
				case "stop":
					Log.logger.info("Stopping System.");
					System.out.print("\b\b\b\b");
					System.exit(0);
				case "discord":
					switch (command[1]) {
						case "start":
							iCee.discord.start();
							break;
						case "state":
							Log.logger.info("Doscord Bot Now State : " + iCee.discord.getState());
							break;
						case "stop":
							iCee.discord.stop();
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
							Log.logger.info("QQ Bot Mirai Now State : " + iCee.mirai.getState());
							break;
						case "stop":
							iCee.mirai.stopMirai();
							break;
						default:
							Log.logger.warn("No option <" + command[1] + "> exist!");
					}
					break;
				default:
					Log.logger.warn("Command <" + command[0] + "> not found.");
			}
			
		}
	}
}
