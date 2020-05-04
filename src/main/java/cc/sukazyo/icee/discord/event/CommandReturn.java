package cc.sukazyo.icee.discord.event;

import cc.sukazyo.icee.discord.RunState;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CommandReturn extends ListenerAdapter {
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		System.out.println("[INFO]Received: \"" + event.getMessage().getContentDisplay() + "\" from \"" + event.getAuthor().getName() + "\"");
		
		if (RunState.shutdownRun) {
			shutdownSure(event);
		} else {
			normalMessageReplay(event);
		}
	}
	
	private void normalMessageReplay (MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) {
			System.out.println("[INFO]Checked self message");
			return;
		}
		
		if (event.getMessage().getContentRaw().equals("ping")) {
			event.getChannel().sendMessage("Pong!").queue();
		} if (event.getMessage().getContentRaw().equals("^# shutdown")) {
			event.getChannel().sendMessage("Are you sure to shutting down?\nType `yes` to make sure.").queue();
			System.out.println("[INFO]Run into Shuttingdown Mode.");
			RunState.shutdownRun = true;
			RunState.eventer = event.getAuthor();
		}
	}
	
	private void shutdownSure (MessageReceivedEvent event) {
		if (event.getAuthor().equals(RunState.eventer)) {
			if (event.getMessage().getContentRaw().equals("yes")) {
				event.getChannel().sendMessage("System Shutdown.").queue();
				System.out.println("[INFO]shutdown.");
				System.exit(0);
			} else {
				event.getChannel().sendMessage("Shutdown canceled.").queue();
				System.out.println("[INFO]Shutdown canceled.");
				RunState.shutdownRun = false;
				RunState.eventer = null;
			}
		}
	}
	
}
