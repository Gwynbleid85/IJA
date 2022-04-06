package ija.app.uml;

public class UMLClass {
	
	private String name;
	private Set<UMLClassAttribute> attributes;
	private Set<String> methods;
	private boolean isInterface;

	public UMLClass(String name){
		this.name = name;
		
	}

}
