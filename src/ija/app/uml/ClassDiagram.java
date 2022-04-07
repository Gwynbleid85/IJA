package ija.app.uml;
import java.util.*;



public class ClassDiagram {
	
	private Set<UMLClass> classes;
	private Set<UMLRelation> relations;

	public ClassDiagram(Set<UMLClass> classes, Set<UMLRelation> relations){ //todo: should have a name?
		this.classes = classes;
		this.relations = relations;
	} 


	public void addClass(UMLClass newClass){

		this.classes.add(newClass);
	}

	public void removeClass(UMLClass newClass){
		this.classes.remove(newClass);
	}

	public void addRelation(UMLRelation newRelation){

		this.relations.add(newRelation);
	}

	public void removeRelation(UMLRelation newRelation){
		this.relations.remove(newRelation);
	}

	/*
	public UMLClass createClass(String name, boolean isInterface) {

		UMLClass newClass = new UMLClass(name, isInterface);
		this.classes.add(newClass);
		return newClass;
	}
	*/
}
