package ija.app.history;

import java.lang.reflect.Method;

public class HistoryEvent {
	private Method method;
	private Object o;
	private Object Args;

	public HistoryEvent(Method method, Object o){
		this.method = method;
		this.o = o;
	}
}
