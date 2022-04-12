package ija.app.uml.classDiagram;
import java.util.*;
public class UMLClass {
	private  List<UMLClassAttribute> attributes;
	private  List<UMLClassMethod> methods;
	private boolean isInterface;

	/**
	 * Constructor of UMLClassDiagram
	 * @param name Name of created UMLClass
	 * @param isInterface Is created UMLClass interface?
	 */
	public UMLClass(String name, boolean isInterface){
		attributes = new LinkedList<UMLClassAttribute>();
		methods = new LinkedList<UMLClassMethod>();

	}

	/**
	 * Constructor of UMLClassDiagram
	 * @param name Name of created class
	 * @note Default value of isInterface attribute is False
	 */
	public UMLClass(String name){
		attributes = new LinkedList<UMLClassAttribute>();
		methods = new LinkedList<UMLClassMethod>();
		isInterface = false;
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
	 * Method deletes attribute of given name from list of attributes of class
	 * @param name name of attribute to be deleted
	 * @return False if attribute with given name doesn't exist, True if successfully removed
	 */
	public boolean delAttribute(String name){
		return attributes.remove(new UMLClassAttribute(name));
	}

	/**
	 * Method to get list of all attributes of class
	 * @return Unmodifiable list of UMLClassAttributes
	 */
	public List<UMLClassAttribute> getAttributes(){
		return Collections.unmodifiableList(attributes);
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
	 * Method deletes UMLMethod of given name from list of UMLMethod of class
	 * @param name Name of UMLMethod to be deleted
	 * @return  False if attribute with given name doesn't exist, True if successfully removed
	 */
	public boolean delMethod(String name){
		return methods.remove(new UMLClassMethod(name));
	}

	/**
	 * Method to get list of all UMLMethods of class
	 * @return Unmodifiable list of UMLClassMethod
	 */
	public List<UMLClassMethod> getMethods(){
		return Collections.unmodifiableList(methods);
	}
}
