package ija.app.history;

import java.io.IOException;

/**
 * @author Milos Hegr (xhegrm00)
 */
public interface HistoryEvent {
	/* Method to undo event */
	public void undo() throws IOException;
}
