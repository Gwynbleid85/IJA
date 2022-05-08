package ija.app.history;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.List;

/**
 * Class representing history and undo/redo actions
 * @author Milos Hegr (xhegrm00)
 * Class implements DP singleton
 */
public class History {

	private static History instance = null;
	private Stage stage;

	/** Stack for undo operations */
	private List<HistoryEventSet> hisStack_1;
	/** Stack for redo operations */
	private List<HistoryEventSet> hisStack_2;

	private int state;
	public boolean wasEvent;
	private boolean goingBack;

	private History(){
		hisStack_1 = new LinkedList<>();
		hisStack_2 = new LinkedList<>();
		wasEvent = true;
		goingBack = false;
		state = 0;
	}

	/**
	 * Method implementing DP singleton
	 * @param scene Where history should be listening
	 * @return Singleton instance
	 */
	public static History getInstance(Scene scene){
		if(instance == null){
			instance = new History();
		}
		addEventHandlers(scene);
		return instance;
	}

	/**
	 * Method to set event handlers
	 * @param scene Where should History listen
	 */
	private static void addEventHandlers(Scene scene){

		/* Reset event adder */
		scene.addEventHandler(MouseEvent.MOUSE_MOVED, e -> {
			instance.wasEvent = true;
		});

		/* Undo and redo */
		scene.addEventHandler(KeyEvent.KEY_PRESSED,  e -> {
			instance.wasEvent = true;
			if( e.getCode() == KeyCode.Z && e.isShortcutDown() && e.isShiftDown()){
				instance.redo();
			}
			else if(e.getCode() == KeyCode.Z && e.isShortcutDown() ){
				instance.undo();
			}
			instance.wasEvent = true;
		});
	}
	public static History getInstance(){
		return instance;
	}

	/**
	 * Add event to History
	 * @param event Event to be added
	 */
	public static void addEvent(HistoryEvent event){
		if(instance != null)
			instance.addEvent2(event);
	}


	/**
	 * Add event to History
	 * @param event Event to be added
	 */
	public void addEvent2(HistoryEvent event){
		if(!goingBack){
			if(wasEvent){
				hisStack_1.add(0, new HistoryEventSet());
				if(hisStack_1.size() > 10)
					hisStack_1.remove(10);
			}
			System.out.println("Added undo event (" + wasEvent + ") (" + event.toString() + ")");
			hisStack_1.get(0).addEvent(event);
		}
		else{
			if(wasEvent){
				hisStack_2.add(0, new HistoryEventSet());
				if(hisStack_2.size() > 10)
					hisStack_2.remove(10);
			}
			System.out.println("Added redo event (" + wasEvent + ") (" + event.toString() + ")");
			hisStack_2.get(0).addEvent(event);
		}
		wasEvent = false;
	}

	/**
	 * Undo operation
	 */
	public static void undoStatic(){
		instance.undo();
	}

	/**
	 * Undo operation
	 */
	public void undo(){
		if(hisStack_1.size() > 0){
			goingBack = true;
			hisStack_1.get(0).undo();
			hisStack_1.remove(0);
			goingBack = false;
		}
	}

	/**
	 * Redo operation
	 */
	public static void redoStatic(){
		instance.redo();
	}

	/**
	 * Redo operation
	 */
	public void redo(){
		if(hisStack_2.size() > 0){
			hisStack_2.get(0).undo();
			hisStack_2.remove(0);
		}
	}

}
