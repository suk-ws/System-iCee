package cc.sukazyo.icee.api;

import cc.sukazyo.icee.system.Log;
import cc.sukazyo.icee.system.Resources;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Random;

public class HumanJoke {
	
	public static String jokeRandom (String who) {
		
		String[] jokes = new String[0];
		try {
			jokes = new Gson().fromJson(Resources.getAssetsAsString("/api/joke.json"), TemplateJokes.class).jokes;
		} catch (IOException e) {
			Log.logger.error(e);
		}
		
		return jokes[new Random().nextInt(jokes.length)].replace("${text}", who);
		
	}
	
}

class TemplateJokes {
	
	String[] jokes;
	
}
