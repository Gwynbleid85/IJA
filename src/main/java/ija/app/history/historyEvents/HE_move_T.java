package ija.app.history.historyEvents;

import ija.app.gui.G_Position;

import java.io.IOException;

/**
 * @author Milos Hegr (xhegrm00)
 */
public interface HE_move_T {
	void moveTo(G_Position pos) throws IOException;
}
