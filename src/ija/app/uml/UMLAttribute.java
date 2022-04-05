package ija.app.uml;

public class UMLAttribute extends Element {
	
	private UMLClassifier type;

	public UMLAttribute(java.lang.String name, UMLClassifier type){
		super(name);
		this.type = type;
	}

	public UMLClassifier getType(){
		return this.type;
	}

	public java.lang.String toString(){
		return super.name + ":" + this.type.toString();
	}
}