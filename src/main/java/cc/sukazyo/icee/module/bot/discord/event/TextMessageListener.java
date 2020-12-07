package cc.sukazyo.icee.module.bot.discord.event;

import cc.sukazyo.icee.module.bot.CommonBotMessage;
import net.dv8tion.jda.api.entities.MessageType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


public class TextMessageListener extends ListenerAdapter {
	
	@Override
	public void onMessageReceived (MessageReceivedEvent event) {
		if (event.getMessage().getType().equals(MessageType.DEFAULT)) {
			new CommonBotMessage(event).doReply();
		}
	}
	
}
