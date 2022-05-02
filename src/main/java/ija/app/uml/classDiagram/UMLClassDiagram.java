package ija.app.uml.classDiagram;
import java.util.*;

/**
 * @author Milos Hegr (xhegrm00)
 * @date 12.4.2022
 * Class representing class diagram
 */
public class UMLClassDiagram {

	private Set<UMLClass> classes;
	private Set<UMLRelation> relations;

	public UMLClassDiagram(){
		this.classes = new HashSet<>();
		this.relations = new HashSet<>();
	}

	public UMLClassDiagram(Set<UMLClass> classes, Set<UMLRelation> relations){
		this.classes = classes;
		this.relations = relations;
	}

	public boolean addClass(UMLClass newClass){
		return this.classes.add(newClass);
	}

	public boolean delClass(String className){
		clearRelations(className);
		return this.classes.remove(new UMLClass(className));
	}
	/**
	 * Method to get Set of all UMLClass objects in UMLClassDiagram
	 * @return Unmodifiable Set all UMLClass objects in UMLClassDiagram
	 */
	public Set<UMLClass> getClasses(){
		return classes;
	}

	public boolean addRelation(UMLRelation newRelation){
		return this.relations.add(newRelation);
	}

	public boolean delRelation(UMLRelation relation){
		return this.relations.remove(relation);
	}

	/**
	 * Method to get Set of all UMLRelation objects in UMLClassDiagram
	 * @return Unmodifiable Set all UMLRelation objects in UMLClassDiagram
	 */
	public Set<UMLRelation> getRelations(){
		return relations;
	}

	/**
	 * Method to clear relations after deleting UMLClass from UMLClassDiagram
	 * @param className Name of deleted class
	 */
	private void clearRelations(String className){
		relations.removeIf(r -> Objects.equals(r.getTo(), className) || (r.getFrom().contains(className) && r.getFrom().size() == 1));
		for(UMLRelation r : relations){
			r.delFrom(className);
		}
	}

	/**
	 * Method to get all own UMLClassMethods from UMLClass with given name
	 * @param className Name of class we want methods from
	 * @return null if UMLClass not found, List of UMLMethods otherwise
	 */
	public List<UMLClassMethod> getUMLClassOwnMethods(String className){
		if(! classes.contains(new UMLClass(className)))
			return new ArrayList<>();
		return findClassByName(className).getMethods();
	}

	/**
	 * Method to get all inherited UMLClassMethods from UMLClass with given name
	 * @param className Name of class we want inherited methods from
	 * @return null if UMLClass not found, List of inherited UMLMethods otherwise
	 */
	public List<UMLClassMethod> getUMLClassInheritedMethods(String className){
		if(! classes.contains(new UMLClass(className)))
			return null;
		UMLClass c = findClassByName(className);
		List<UMLClassMethod> list = new ArrayList<>(getUMLClassInheritedMethodsHelper(c));
		list.removeAll(c.getMethods());
		return list;
	}

	/**
	 * Helper method for getUMLClassesInheritedMethods method
	 * Goes recursively through full inherit tree and gets all inherited UMLMethods
	 * @param c UMLClass of which we want to get all UMLMethods (own and inherited)
	 * @return List of all UMLMethods (onw and inherited)
	 */
	private List<UMLClassMethod> getUMLClassInheritedMethodsHelper(UMLClass c){
		List<UMLClassMethod> list = new ArrayList<>(c.getMethods());
		/* Got through all relations and find all classes that class c is inheriting from*/
		for(UMLRelation r : relations){
			if(r.getFrom().contains(c.getName()) && Objects.equals(r.getType(), "in"))
				/* Recursively call this method */
				list.addAll(new LinkedList<>(getUMLClassInheritedMethodsHelper(findClassByName(r.getTo()))));
		}
		return list;
	}

	/**
	 * Method for finding UMLClass object from classes attribute by name
	 * @param name Name of wanted UMLClass object
	 * @return tmp UMLClass object with name "not found" if not found, wanted UMLClass object otherwise
	 */
	private UMLClass findClassByName(String name){
		for (UMLClass c : classes) {
			if ( c.equals(new UMLClass(name)))
			return c ;
		}
		return new UMLClass("not found");
	}

}
