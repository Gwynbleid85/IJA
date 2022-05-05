package ija.app.history.historyEvents;

import ija.app.gui.G_Position;
import ija.app.history.HistoryEvent;

import java.io.IOException;

public class HE_move  implements HistoryEvent {

	private G_Position pos;
	private HE_move_T origin;
	public HE_move(HE_move_T origin, G_Position pos){
		this.origin = origin;
		this.pos = pos;
	}
	@Override
	public void undo() throws IOException {
		origin.moveTo(pos);
	}
}
