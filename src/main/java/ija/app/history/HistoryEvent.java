package ija.app.history;

import java.io.IOException;

public interface HistoryEvent {
	public void undo() throws IOException;
}
