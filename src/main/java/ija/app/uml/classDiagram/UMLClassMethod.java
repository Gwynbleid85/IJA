package ija.app.uml.classDiagram;


/**
 * @author Milos Hegr (xhegrm00)
 * @date 9.4.2022
 * Class representing method in UML class
 */

public class UMLClassMethod {
	private String name;
	private String accessMod;

	/**
	 * Constructor of UMLClassMethod
	 * @param name name of created UMLMethod
	 * @param accessMod Access modifier of created UMLMethod
	 */
	public UMLClassMethod(String name, String accessMod){
		this.name = name;
		this.accessMod = accessMod;
	}

	/**
	 * Constructor of UMLClassMethod
	 * @param name name of created UMLMethod
	 * @note Default value of accessMod attribute is empty string
	 */
	public UMLClassMethod(String name){
		this.name = name;
		accessMod = "";
	}

	/**
	 * Getter of name attribute
	 * @return name attribute of UMLClass object
	 */
	public String getName(){
		return name;
	}

	/**
	 * Setter of name attribute
	 * @param name New value of name attribute of UMLClass object
	 */
	public void setName(String name){
		this.name = name;
	}

	/**
	 * Getter of AccessMod attribute
	 * @return AccessMod attribute of UMLClass object
	 */
	public String getAccessMod(){
		return accessMod;
	}

	/**
	 * Setter of AccessMod attribute
	 * @param accessMod New value of accessMod attribute of UMLClass object
	 */
	public void setAccessMod(String accessMod){
		this.accessMod = accessMod;
	}

	@Override
	public String toString() {
		return accessMod + " " + name + "()";
	}

	@Override
	public boolean equals(Object o) {
		if(! (o instanceof UMLClassMethod)) return false;

		UMLClassMethod c = (UMLClassMethod) o;
		return this.name.equals(c.name);
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
}