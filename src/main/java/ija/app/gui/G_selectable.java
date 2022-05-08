package ija.app.gui;

/**
 * @author Milos Hegr (xhegrm00)
 */
public interface G_selectable {
	/**
	 * Method to set Object to selected
	 * @param selected is selected
	 */
	void setSelect(boolean selected);

	/**
	 * Get type of selected object
	 * ( UMLClass, UMLRelation )
	 * @return type of selected object
	 */
	String getType();
}
