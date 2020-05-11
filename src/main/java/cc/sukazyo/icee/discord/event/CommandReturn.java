package cc.sukazyo.icee.discord.event;

import cc.sukazyo.icee.discord.system.Lang;
import cc.sukazyo.icee.discord.system.RunState;
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
			event.getChannel().sendMessage(Lang.get("reply.ping")).queue();
		} if (event.getMessage().getContentRaw().equals("^# shutdown")) {
			event.getChannel().sendMessage(Lang.get("command.shutdown.start")).queue();
			RunState.shutdownRun = true;
			RunState.eventer = event.getAuthor();
		}
	}
	
	private void shutdownSure (MessageReceivedEvent event) {
		if (event.getAuthor().equals(RunState.eventer)) {
			if (event.getMessage().getContentRaw().equals("yes")) {
				event.getChannel().sendMessage(Lang.get("command.shutdown.confirm")).queue();
				System.out.println("[INFO]shutdown.");
				System.exit(0);
			} else {
				event.getChannel().sendMessage(Lang.get("command.shutdown.cancel")).queue();
				System.out.println("[INFO]Shutdown canceled.");
				RunState.shutdownRun = false;
				RunState.eventer = null;
			}
		}
	}
	
}
