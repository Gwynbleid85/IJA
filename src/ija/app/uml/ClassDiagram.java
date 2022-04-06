package ija.app.uml;

import java.util.*;
public class ClassDiagram {
	
	private Set<UMLClass> classes;
	private Set<UMLRelation> relations;

	public ClassDiagram(java.lang.String name){
		//super(name);
		//this.classifiers = new HashSet<UMLClassifier> ();
	}

	public UMLClass createClass(java.lang.String name) {

		UMLClass newClass = new UMLClass(name);
		/*
		if(this.classifiers.contains(newClass))
			return null;
		
		this.classifiers.add(newClass);
		*/
		return newClass;
	}
}
