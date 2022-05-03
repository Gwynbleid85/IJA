package ija.app.uml.classDiagram;
import java.util.*;

/**
 * @author Milos Hegr (xhegrm00)
 * @date 11.4.2022
 * Class representing class in UML
 */

public class UMLClass {
	private String name;
	private  List<UMLClassAttribute> attributes;
	private  List<UMLClassMethod> methods;
	private boolean isInterface;

	/**
	 * Constructor of UMLClassDiagram
	 * @param name Name of created UMLClass
	 * @param isInterface Is created UMLClass interface?
	 */
	public UMLClass(String name, boolean isInterface){
		this.name = name;
		attributes = new LinkedList<>();
		methods = new LinkedList<>();
	}

	/**
	 * Constructor of UMLClassDiagram
	 * @param name Name of created class
	 * @note Default value of isInterface attribute is False
	 */
	public UMLClass(String name){
		this.name = name;
		attributes = new LinkedList<>();
		methods = new LinkedList<>();
		isInterface = false;
	}

	/**
	 * Getter of attribute name
	 * @return Value of attribute name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter of attribute name
	 * @param name New value for attribute name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Method to check if class is interface
	 * @return True if is interface.
	 */
	public boolean isInterface(){
		return isInterface;
	}

	/**
	 * Method change interface status
	 * @param isInterface Is interface?
	 */
	public void setIsInterface(boolean isInterface){
		this.isInterface = isInterface;
	}

	/**
	 * Method adds attribute to list of attributes of class
	 * @param attrib Attribute to be added
	 * @return  False if given attribute already exists, True if success
	 */
	public boolean addAttribute(UMLClassAttribute attrib){
		if(attributes.contains(attrib))
			return false;
		return attributes.add(attrib);
	}

	/**
	 * Method deletes all attributes from list of attributes of class
	 */
	public void delAttributes(){
		attributes = new LinkedList<>();
	}

	/**
	 * Method to get list of all attributes of class
	 * @return Unmodifiable list of UMLClassAttributes
	 */
	public List<UMLClassAttribute> getAttributes(){
		return attributes;
	}

	/**
	 * Method adds UMLMethod to list of methods of class
	 * @param method UMLMethod to be added
	 * @return  False if given UMLMethod already exists, True if success
	 */
	public boolean addMethod(UMLClassMethod method){
		if(methods.contains(method))
			return false;
		return methods.add(method);
	}

	/**
	 * Method deletes all UMLMethods from list of UMLMethod of class
	 */
	public void delMethods(){
		methods = new LinkedList<>();
	}

	/**
	 * Method to get list of all UMLMethods of class
	 * @return Unmodifiable list of UMLClassMethod
	 */
	public List<UMLClassMethod> getMethods(){
		return methods;
	}


	@Override
	public boolean equals(Object o) {
		if(! (o instanceof UMLClass)) return false;

		UMLClass c = (UMLClass) o;
		return this.name.equals(c.name);
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
}
