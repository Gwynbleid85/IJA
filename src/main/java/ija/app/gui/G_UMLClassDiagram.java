package ija.app.gui;

import ija.app.uml.classDiagram.UMLClass;
import ija.app.uml.classDiagram.UMLClassDiagram;
import ija.app.uml.classDiagram.UMLRelation;
import javafx.scene.Group;
import javafx.scene.Node;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class G_UMLClassDiagram {
	private UMLClassDiagram diagram;
	private Set<G_UMLClass> classes;
	private Set<G_UMLRelation> relations;

	private Group root;

	public G_UMLClassDiagram(UMLClassDiagram d) throws IOException {
		diagram = d;
		root = new Group();

		/* Create gui classes*/
		classes = new HashSet<>();
		for(UMLClass c : d.getClasses()){
			G_UMLClass gc = new G_UMLClass(c, this);
			classes.add(gc);
			root.getChildren().add(gc.getNode());
		}

		/* Create gui relations*/
		relations = new HashSet<>();
		for(UMLRelation r : d.getRelations()){
			G_UMLRelation gr = new G_UMLRelation(r, this);
			relations.add(gr);
			root.getChildren().add(gr.getNode());
		}

	}


	public G_UMLClass getGUMLClass(String name){
		for(G_UMLClass gc : classes){
			if(Objects.equals(gc.getName(), name)){
				return gc;
			}
		}
		return null;
	}

	public void updateRelations(){
		for(G_UMLRelation r : relations){
			r.updatePos();
		}
	}

	public Node getNode(){return root;}

}
