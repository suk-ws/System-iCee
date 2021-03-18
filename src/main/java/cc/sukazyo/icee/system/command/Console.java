package cc.sukazyo.icee.system.command;

import cc.sukazyo.icee.system.Log;
import cc.sukazyo.icee.util.CommandHelper;

import java.util.Arrays;
import java.util.Scanner;

public class Console {
	
	private static class Listener extends Thread {
		
		private static final String THREAD_NAME = "Console";
		
		private final Scanner scanner = new Scanner(System.in);
		
		protected Listener () {
			setName(THREAD_NAME);
		}
		
		@Override
		public void run () {
			
			setName(THREAD_NAME);
			
			String lastLine;
			//noinspection InfiniteLoopStatement
			while (true) {
				try {
					
					lastLine = scanner.nextLine();
					Log.logger.info("Console execute command: " + lastLine);
					try {
						final String[] args = CommandHelper.format(lastLine);
						Log.logger.trace("Parse:" + Arrays.toString(args));
						CommandManager.execute(args);
					} catch (CommandException.ParameterDuplicatedException | CommandException.CommandNotFoundException | CommandException.ParameterValueUnavailableException e) {
						Log.logger.error("The command cannot be executed due to the following reasons:\n" + e.getMessage());
					}
					
				} catch (Exception e) {
					Log.logger.error("Some unexpected exception occurred while executing command: ", e);
				}
			}
			
		}
		
	}
	private static final Listener listener = new Listener();
	
	public static void start () {
		listener.start();
	}
	
}
