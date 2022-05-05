package ija.app.history.historyEvents;

import ija.app.gui.G_selectable;

public interface HE_addAndDelete_T {
	void addNew(G_selectable newObject);
	void delete(G_selectable toDelete);
}
