package ija.app.gui;

import ija.app.uml.classDiagram.UMLRelation;
import javafx.scene.Node;
import javafx.scene.shape.Line;

public class G_UMLRelation {
	private UMLRelation relation;

	private Line root;
	public G_UMLRelation(UMLRelation r){relation = r;
		root = new Line();
	}

	public Node getNode(){return root;}
}
