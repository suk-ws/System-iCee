package cc.sukazyo.icee.bot.discord.event;

import cc.sukazyo.icee.bot.BotHelper;
import cc.sukazyo.icee.system.Lang;
import cc.sukazyo.icee.system.RunState;
import cc.sukazyo.icee.util.CommandHelper;
import cc.sukazyo.icee.system.Log;
import net.dv8tion.jda.api.entities.MessageType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.regex.Pattern;

public class TextMessageListener extends ListenerAdapter {
	
	@Override
	public void onMessageReceived (MessageReceivedEvent event) {
		
		if (event.getMessage().getType().equals(MessageType.DEFAULT)) {
			
			Log.logger.debug(
					"From <" + event.getAuthor().getName() +
							"> on [" + event.getChannel().getName() +
							"] received following Message:\n" + event.getMessage().getContentRaw());
			
			if (RunState.getState(event.getAuthor()) == -1) {
				Log.logger.debug("No exist event");
				
				String exec = BotHelper.isBotCalled(event.getMessage().getContentRaw(), event.getMessage().getContentDisplay());
				if (exec != null) {
					if (exec.startsWith("$ ")) {
						
						String[] comm = CommandHelper.format(exec.replaceFirst("\\$ ", ""));
						Log.logger.debug("Called command : " + comm[0]);
						event.getChannel().sendMessage(CommandReturn.command(comm, event)).queue();
						
					} else {
						iceeAtReturn(event);
					}
				}
			} else {
				Log.logger.debug("Checked exist event on this user");
			}
		}
		
	}
	
	private void iceeAtReturn (MessageReceivedEvent event) {
		Log.logger.debug("icee hello");
		event.getChannel().sendMessage(Lang.get("reply.hi")).queue();
	}
	
}
