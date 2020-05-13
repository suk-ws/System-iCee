package cc.sukazyo.icee.discord.event;

import cc.sukazyo.icee.discord.system.Lang;
import cc.sukazyo.icee.discord.system.RunState;
import cc.sukazyo.icee.discord.util.CommandHelper;
import cc.sukazyo.icee.discord.util.Log;
import net.dv8tion.jda.api.entities.MessageType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.regex.Pattern;

public class TextMessageListener extends ListenerAdapter {
	
	@Override
	public void onMessageReceived (MessageReceivedEvent event) {
		
		if (event.getMessage().getType().equals(MessageType.DEFAULT)) {
			
			Log.debug(
					"From <" + event.getAuthor().getName() +
							"> on [" + event.getChannel().getName() +
							"] received following Message:\n" + event.getMessage().getContentRaw());
			
			if (RunState.getState(event.getAuthor()) == -1) {
				Log.debug("No exist event");
				
				if (event.getMessage().getContentDisplay().startsWith("@iCee $ ")) {
					
					String[] comm = CommandHelper.format(event.getMessage().getContentDisplay().replaceFirst("@iCee \\$ ", ""));
					Log.debug("Called command : " + comm[0]);
					event.getChannel().sendMessage(CommandReturn.command(comm, event)).queue();
					
				} else if (event.getMessage().getContentDisplay().contains("@iCee")) {
					
					iceeAtReturn(event);
					
				}
				
			} else {
				Log.debug("Checked exist event on this user");
			}
		}
		
	}
	
	private void iceeAtReturn (MessageReceivedEvent event) {
		if (Pattern.matches("^\\s*@iCee\\s*$", event.getMessage().getContentDisplay())) {
			Log.debug("icee hello");
			event.getChannel().sendMessage(Lang.get("reply.hi")).queue();
		} else {
			Log.info("Caught unexist things");
			event.getChannel().sendMessage(Lang.get("reply.noreply")).queue();
		}
	}
	
}
