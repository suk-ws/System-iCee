package cc.sukazyo.icee.bot;

import cc.sukazyo.icee.system.Lang;
import cc.sukazyo.icee.system.Log;
import cc.sukazyo.icee.system.Variable;
import cc.sukazyo.icee.util.CommandHelper;
import cc.sukazyo.icee.util.FileHelper;
import cc.sukazyo.icee.util.SimpleUtils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.mamoe.mirai.message.FriendMessageEvent;
import net.mamoe.mirai.message.GroupMessageEvent;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用 Bot 消息对象，用于统一 Bot 消息的处理
 */
public class CommonBotMessage {
	
	public final Type type;
	
	Message returnMsg;
	
	Message msg;
	
	public MessageReceivedEvent jdaEvent;
	public GroupMessageEvent miraiGroupEvent;
	public FriendMessageEvent miraiFriendEvent;
	
	/**
	 * Discord Bot Message 构造
	 *
	 * @param event 处理的event
	 */
	public CommonBotMessage(MessageReceivedEvent event) {
		type = Type.DISCORD;
		jdaEvent = event;
		msg = new Message(event.getMessage().getContentRaw(), event.getMessage().getContentDisplay());
		Log.logger.debug("From <" + event.getChannel().getName() +
						">[" + event.getAuthor().getName() +
						"] Received : \n\t" +
						event.getMessage().getContentRaw().replaceAll("\\n", "\n\t")
		);
		event.getMessage().getAttachments().forEach(node -> {
			if (node.isImage()) {
				Log.logger.info("IMAGE");
				if (!event.getAuthor().isBot()) {
					try {
						File msg = new File("./data/" + SimpleUtils.randomId() + ".png");
						CompletableFuture<File> task = node.downloadToFile(msg);
						long time = System.currentTimeMillis();
						while (!task.isDone()) { if (System.currentTimeMillis() > time+10000) throw new InterruptedException("Download time out"); }
						Log.logger.info("Reupload image called" + msg.getName());
						event.getChannel().sendFile(msg).queue();
						Thread.sleep(10000);
						msg.delete();
					} catch (InterruptedException e) {
						Log.logger.error("Some error excepted while waiting", e);
						event.getChannel().sendMessage(Lang.get("bot.repeat-error")).queue();
					}
				}
			} else if (node.isVideo())
				Log.logger.info("VIDEO");
			else
				Log.logger.info("DEFAULT");
		});
	}
	
	/**
	 * Mirai QQ 群组 Message 构造
	 *
	 * @param event 处理的event
	 */
	public CommonBotMessage(GroupMessageEvent event) {
		type = Type.MIRAI_GROUP;
		miraiGroupEvent = event;
		msg = new Message(event.getMessage().contentToString(), event.getMessage().contentToString());
		Log.logger.debug(
				"From <" +
				event.getGroup().getName() +
				">[" + event.getSenderName() +
				"] Received : \n\t" +
				event.getMessage().contentToString().replaceAll("\\n", "\n\t")
		);
	}
	
	/**
	 * Mirai QQ 的 QQ “朋友” Message 构造
	 *
	 * @param event 处理的event
	 */
	public CommonBotMessage(FriendMessageEvent event) {
		type = Type.MIRAI_FRIEND;
		miraiFriendEvent = event;
		msg = new Message(event.getMessage().contentToString(), event.getMessage().contentToString());
		Log.logger.debug(
				"From [" + event.getSenderName() +
				"] Received : \n\t" +
				event.getMessage().contentToString().replaceAll("\\n", "\n\t")
		);
	}
	
	public void doReply() {
		
		// 检查消息是否呼叫了 Bot
		String call = BotHelper.isBotCalled(this);
		if (call == null) { return; }
		
		// 定义返回字符串
		String returned;
		
		Matcher commander = Pattern.compile("^\\s*\\$\\s*([\\s\\S]*)").matcher(call);
		if (commander.find()) {
			Log.logger.debug("Process command : " + commander.group(1));
			returned = CommandReturn.command(CommandHelper.format(commander.group(1)), this);
		} else {
			try {
				returned = FileHelper.getDataContent("./data/debug.txt");
			} catch (IOException e) {
				returned = "Error";
			}
		}
		
		returnMsg = new Message(Variable.compile(returned, this));
		
		switch (type) {
			case DISCORD:
				Log.logger.debug("Echo : " + returnMsg);
				jdaEvent.getChannel().sendMessage(returnMsg.getRaw()).queue();
				break;
			case MIRAI_FRIEND:
				miraiFriendEvent.getSender().sendMessage(returnMsg.getRaw());
				break;
			case MIRAI_GROUP:
				miraiGroupEvent.getGroup().sendMessage(returnMsg.getRaw());
				break;
		}
		
	}
	
	public enum Type {
		DISCORD, MIRAI_GROUP, MIRAI_FRIEND
	}
	
	public String getMessageRaw () { return msg.getRaw() ; }
	
	public String getMessageText () { return msg.getText(); }
	
}

/**
 * 单个的消息
 */
class Message {
	
	Type type;
	
	private String raw;
	private String text;
	
	// 下一个消息对象的指针，用于链性消息
	Message next = null;
	
	public Message (String raw, String text) {
		this.raw = raw;
		this.text = text;
		type = Type.TEXT;
	}
	
	public Message(String msg) {
		this.raw = msg;
		type = Type.TEXT;
	}
	
	public String getRaw() {
		StringBuilder msg = new StringBuilder();
		if (type == Type.TEXT) {
			msg.append(raw);
		}
		if (next != null) {
			msg.append(next.getRaw());
		}
		return msg.toString();
	}
	
	public String getText() {
		StringBuilder msg = new StringBuilder();
		if (type == Type.TEXT) {
			msg.append(text);
		}
		if (next != null) {
			msg.append(next.getText());
		}
		return msg.toString();
	}
	
	public void setRawAndFlush (String msg) {
		raw = msg;
		next = null;
	}
	
	// 向链中添加一个消息
	public void append (Message next) {
		if (this.next == null) {
			this.next = next;
		} else {
			this.next.append(next);
		}
	}
	
	enum Type {
		TEXT, IMAGE, VOICE, FILE
	}
	
}