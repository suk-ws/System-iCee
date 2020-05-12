package cc.sukazyo.icee.discord.event;

import cc.sukazyo.icee.discord.system.Lang;
import cc.sukazyo.icee.discord.system.RunState;
import cc.sukazyo.icee.discord.util.Log;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandReturn extends ListenerAdapter {

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		Log.debug("Received: \"" + event.getMessage().getContentDisplay() + "\" from \"" + event.getAuthor().getName() + "\"");

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

		String todo;
		if (event.getMessage().getContentRaw().equals("ping")) {
			event.getChannel().sendMessage(Lang.get("reply.ping")).queue();
		} if (event.getMessage().getContentRaw().equals("^# shutdown")) {
			event.getChannel().sendMessage(Lang.get("command.shutdown.start")).queue();
			RunState.shutdownRun = true;
			RunState.eventer = event.getAuthor();
		} else if (event.getMessage().getContentRaw().equals("=userinfo")) {
			Log.debug("userinfo");
			todo = event.getAuthor().getAvatarId() + '\n';
			todo += event.getAuthor().getDefaultAvatarId() + '\n';
			todo += event.getAuthor().getAvatarUrl() + '\n';
			todo += event.getAuthor().getDefaultAvatarUrl() + '\n';
			todo += event.getAuthor().getName() + '\n';
			todo += event.getAuthor().getId() + '\n';
			todo += event.getAuthor().toString() + '\n';
			Log.debug(todo);
			event.getChannel().sendMessage(todo).queue();
		} else if (event.getMessage().getContentRaw().equals("=channel")) {
			Log.debug("channel");
			todo = event.getChannel().toString();
			Log.debug(todo);
			event.getChannel().sendMessage(todo).queue();
		} else if (event.getMessage().getContentRaw().equals("=private")) {
			Log.debug("private");
			event.getAuthor().openPrivateChannel().complete().sendMessage("privating...").queue();
		} else if (event.getMessage().getContentRaw().equals("=at me")) {
			Log.debug("at me");
			todo = event.getAuthor().getAsMention() + ' ' + "hi";
			Log.debug(todo);
			event.getAuthor().getJDA().retrieveUserById(event.getAuthor().getId()).queue();
			event.getChannel().sendMessage(todo).queue();
		}
	}

	private void shutdownSure (MessageReceivedEvent event) {
		if (event.getAuthor().equals(RunState.eventer)) {
			if (event.getMessage().getContentRaw().equals("yes")) {
				event.getChannel().sendMessage(Lang.get("command.shutdown.confirm")).queue();
				Log.info("shutdown");
				System.exit(0);
			} else {
				event.getChannel().sendMessage(Lang.get("command.shutdown.cancel")).queue();
				Log.info("shutdown canceled");
				RunState.shutdownRun = false;
				RunState.eventer = null;
			}
		}
	}

}
