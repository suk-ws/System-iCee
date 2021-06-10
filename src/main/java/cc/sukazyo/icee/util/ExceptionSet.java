package cc.sukazyo.icee.util;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ExceptionSet extends Throwable {
	
	List<Pair<Exception, String>> exceptions;
	
	public ExceptionSet () {
		exceptions = new LinkedList<>();
	}
	
	public void add (Exception e, String extraInformation) {
		add(new Pair<>(e, extraInformation));
	}
	
	public void add (Pair<Exception, String> ex) {
		exceptions.add(ex);
	}
	
	public void forEach (BiConsumer<Exception, String> action) {
		forEach(ex -> action.accept(ex.a, ex.b));
	}
	
	public void forEach (Consumer<Pair<Exception, String>> action) {
		exceptions.forEach(action);
	}
	
	public boolean isEmpty () {
		return exceptions.isEmpty();
	}
	
	public List<Pair<Exception, String>> getList () {
		return exceptions;
	}
	
}
