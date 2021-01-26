package cc.sukazyo.icee.module.bot;

import cc.sukazyo.icee.system.Log;
import cc.sukazyo.icee.system.Variable;
import cc.sukazyo.icee.util.CommandHelper;
import cc.sukazyo.icee.util.FileHelper;
import com.google.gson.Gson;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
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
		StringBuilder messageDescrbition = new StringBuilder();
		if (event.getMessage().isSuppressedEmbeds()) {
			event.getMessage().getEmbeds().forEach(node ->
				messageDescrbition.append("EMBED Message : ").append(new Gson().toJson(node.toData())).append('\n'));
		} else {
			event.getMessage().getAttachments().forEach(node -> {
				if (node.isImage()) {
					try {
						appendMessage(new Message(node.retrieveInputStream().get(), node.getFileName(), Message.Type.IMAGE));
						messageDescrbition.append("IMAGE Message : ").append(node.getUrl()).append("\n");
					} catch (ExecutionException | InterruptedException e) {
						messageDescrbition.append("IMAGE Message Error of ").append(node.getUrl()).append("\n");
						Log.logger.error("Some error excepted while opening file stream", e);
					}
				} else if (node.isVideo()) {
					try {
						appendMessage(new Message(node.retrieveInputStream().get(), node.getFileName(), Message.Type.VIDEO));
						messageDescrbition.append("VIDEO Message : ").append(node.getUrl()).append("\n");
					} catch (ExecutionException | InterruptedException e) {
						messageDescrbition.append("VIDEO Message Error of ").append(node.getUrl()).append("\n");
						Log.logger.error("Some error excepted while opening file stream", e);
					}
				} else {
					try {
						appendMessage(new Message(node.retrieveInputStream().get(), node.getFileName(), Message.Type.FILE));
						messageDescrbition.append("FILE Message : ").append(node.getUrl()).append("\n");
					} catch (ExecutionException | InterruptedException e) {
						messageDescrbition.append("FILE Message Error of ").append(node.getUrl()).append("\n");
						Log.logger.error("Some error excepted while opening file stream", e);
					}
				}
			});
		}
		if (!event.getMessage().getContentRaw().equals("")) {
			messageDescrbition.append(event.getMessage().getContentRaw());
			msg = new Message(event.getMessage().getContentRaw(), event.getMessage().getContentDisplay());
		}
		Log.logger.debug("From <" + event.getChannel().getName() +
				">[" + event.getAuthor().getName() +
				"] Received : \n\t" +
				messageDescrbition.toString().replaceAll("\\n", "\n\t")
		);
	}
	
	/**
	 * Mirai QQ 群组 Message 构造
	 *
	 * @param event 处理的event
	 */
	public CommonBotMessage(GroupMessageEvent event) {
		type = Type.MIRAI_GROUP;
		miraiGroupEvent = event;
		StringBuilder msgRev = new StringBuilder();
		event.getMessage().forEach(node -> {
			if (node instanceof net.mamoe.mirai.message.data.Image) {
				Log.logger.info("IMAGE");
			} else if (node instanceof net.mamoe.mirai.message.data.PlainText) {
				Log.logger.info("PLAIN_TEXT");
			} else if (node instanceof net.mamoe.mirai.message.data.At) {
				Log.logger.info("AT");
			} else if (node instanceof net.mamoe.mirai.message.data.Face) {
				Log.logger.info("FACE");
			} else if (node instanceof net.mamoe.mirai.message.data.Voice) {
				Log.logger.info("VOICE");
			} else {
				Log.logger.info("UNDEFIENED");
			}
		});
		if(!event.getMessage().contentToString().equals("")) {
			msgRev.append(event.getMessage().contentToString());
			appendMessage(new Message(event.getMessage().contentToString()));
		}
		Log.logger.debug(
				"From <" +
				event.getGroup().getName() +
				">[" + event.getSenderName() +
				"] Received : \n\t" +
				msgRev.toString().replaceAll("\\n", "\n\t")
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
	
//		if (type == Type.DISCORD && jdaEvent.getChannel().getIdLong() == 730371639723950158L) {
//			if (jdaEvent.getAuthor().isBot()) { return; }
//			if (msg.type == Message.Type.TEXT) {
//				iCee.mirai.bot.getGroup(651637726).sendMessage(msg.getText() + "\n===========\nby " + jdaEvent.getAuthor().getName());
//			} else if (msg.type == Message.Type.IMAGE) {
//				MessageChain upd = iCee.mirai.bot.getGroup(651637726).uploadImage(msg.getFileStream()).plus("\n===========\nby " + jdaEvent.getAuthor().getName());
//				iCee.mirai.bot.getGroup(651637726).sendMessage(upd);
//			}
//			return;
//		}
		
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
				returned = FileHelper.getDataContent("/debug.txt");
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
	
	private void appendMessage (Message msg) {
		if (this.msg == null) {
			this.msg = msg;
		} else {
			this.msg.append(msg);
		}
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
	
	private InputStream stream;
	private String fileName;
	
	// 下一个消息对象的指针，用于链性消息
	Message next = null;
	
	public Message (String raw, String text) {
		this.raw = raw;
		this.text = text;
		type = Type.TEXT;
	}
	
	public Message(String msg) {
		this.raw = this.text = msg;
		type = Type.TEXT;
	}
	
	public Message (InputStream stream, String fileName, Type type) {
		assert type == Type.FILE || type == Type.IMAGE || type == Type.VIDEO;
		this.type = type;
		this.fileName = fileName;
		this.stream = stream;
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
	
	public String getText () {
		StringBuilder msg = new StringBuilder();
		if (type == Type.TEXT) {
			msg.append(text);
		}
		if (next != null) {
			msg.append(next.getText());
		}
		return msg.toString();
	}
	
	public InputStream getFileStream () {
		return stream;
	}
	
	public String getFileName () { return fileName; }
	
	public String getFileSuffix () { return fileName.substring(fileName.lastIndexOf('.')); }
	
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
		TEXT, IMAGE, VIDEO, VOICE, FILE
	}
	
}