package ija.app.history.historyEvents;

import ija.app.gui.G_selectable;
import ija.app.history.HistoryEvent;

import java.io.IOException;

/**
 * @author Milos Hegr (xhegrm00)
 */
public class HE_addNew implements HistoryEvent {
	private HE_addAndDelete_T origin;
	private G_selectable addNew;

	public HE_addNew(HE_addAndDelete_T origin, G_selectable addNew){
		this.origin = origin;
		this.addNew = addNew;
	}

	public void undo(){
		origin.delete(addNew);
	}
}
