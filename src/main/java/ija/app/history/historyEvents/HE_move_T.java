package ija.app.history.historyEvents;

import ija.app.gui.G_Position;

import java.io.IOException;

public interface HE_move_T {
	void moveTo(G_Position pos) throws IOException;
}
