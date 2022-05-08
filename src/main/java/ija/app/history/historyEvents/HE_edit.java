package ija.app.history.historyEvents;

import ija.app.history.HistoryEvent;

/**
 * @author Milos Hegr (xhegrm00)
 */
public class HE_edit implements HistoryEvent {

	private HE_edit_T origin;
	private Object copy;

	public HE_edit(HE_edit_T origin, Object copy){
		this.origin = origin;
		this.copy = copy;
	}

	public void undo(){
		origin.loadCopy(copy);
	}
}
