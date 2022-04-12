package ija.app.uml.classDiagram;
import java.util.*;



public class ClassDiagram {

	private Set<UMLClass> classes;
	private Set<UMLRelation> relations;

	public ClassDiagram(Set<UMLClass> classes, Set<UMLRelation> relations){ //todo: should have a name?
		this.classes = classes;
		this.relations = relations;
	}

	public ClassDiagram(){ //todo: should have a name?
		this.classes = new HashSet<UMLClass>();
		this.relations = new HashSet<UMLRelation>();
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


	public List<UMLClassMethod> getUMLClassOwnMethods(String className){
		if(! classes.contains(new UMLClass(className)))
			return null;
		return new LinkedList<UMLClassMethod>();
	}

	public List<UMLClassMethod> getUMLClassInheritedMethods(String ClassName){
		return new LinkedList<UMLClassMethod>();
	}

	public Set<UMLClass> getClasses(){
		return Collections.unmodifiableSet(classes);
	}


}
