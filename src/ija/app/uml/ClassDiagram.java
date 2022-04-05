package ija.app.uml;

import java.util.*;
public class ClassDiagram extends Element{
	
	private Set<UMLClassifier> classifiers;

	public ClassDiagram(java.lang.String name){
		super(name);
		this.classifiers = new HashSet<UMLClassifier> ();
	}

	public UMLClass createClass(java.lang.String name) {
		UMLClass newClass = new UMLClass(name);
		if(this.classifiers.contains(newClass))
			return null;
		
		this.classifiers.add(newClass);
		return newClass;
	}

	public UMLClassifier classifierForName(java.lang.String name) {
		this.classifiers.add(UMLClassifier.forName(name));
		for(UMLClassifier c : this.classifiers){
			if(c.name == name)
			return c;
		}
		return null;
	}

	public UMLClassifier findClassifier(java.lang.String name) {
		for(UMLClassifier c : this.classifiers){
			if(c.name == name)
			return c;
		}
		return null;
	}

}
