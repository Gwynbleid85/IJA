package ija.app.history;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Milos Hegr (xhegrm00)
 * Class to group History events
 */
public class HistoryEventSet {
	private List<HistoryEvent> events;
	public HistoryEventSet(){
		events = new LinkedList<>();
	}

	public void addEvent(HistoryEvent event){
		events.add(0, event);
	}

	/* Undo all history events in set */
	public void undo(){
		for(HistoryEvent e : events){
			try{
				e.undo();
			}
			catch (Exception ignored){}
		}
	}
}
