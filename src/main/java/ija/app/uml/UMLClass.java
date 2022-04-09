package ija.app.uml;
import java.util.*;
public class UMLClass {
	
	private String name;
	private Set<UMLClassAttribute> attributes;
	private Set<UMLClassMethod> methods;
	private boolean isInterface;

	public UMLClass(String name, Set<UMLClassAttribute> attributes, Set<UMLClassMethod> methods, boolean isInterface){
		this.name = name;
		this.attributes = attributes;
		this.methods = methods;
		this.isInterface = isInterface;
	}

}
