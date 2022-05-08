package ija.app.history.historyEvents;

import ija.app.gui.G_selectable;
import ija.app.history.HistoryEvent;

/**
 * @author Milos Hegr (xhegrm00)
 */
public class HE_delete implements HistoryEvent {

	private HE_addAndDelete_T origin;
	private G_selectable deleted;

	public HE_delete(HE_addAndDelete_T origin, G_selectable deleted){
		this.origin = origin;
		this.deleted = deleted;
	}

	public void undo(){
		System.out.println("Undo delete (" + deleted.getType() + ")");
		origin.addNew(deleted);
	}
}
