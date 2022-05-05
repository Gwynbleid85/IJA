package ija.app.gui;

public interface G_selectable {

	void setSelect(boolean selected);

	/**
	 * Get type of selected object
	 * ( UMLClass, UMLRelation )
	 * @return type of selected object
	 */
	String getType();
}
