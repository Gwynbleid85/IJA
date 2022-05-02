package ija.app.gui;

import ija.app.uml.classDiagram.UMLRelation;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.shape.Line;

import java.io.IOException;

public class G_UMLRelation {
	private UMLRelation relation;
	private G_UMLClassDiagram diagram;
	private Line root;
	public G_UMLRelation(UMLRelation r, G_UMLClassDiagram d) throws IOException {
		relation = r;
		diagram = d;
		root = FXMLLoader.load(getClass().getResource("fxml/G_UMLRelation.fxml"));

		updatePos();
		System.out.println("Pos updated");
	}

	public void updatePos(){
		G_Position from;
		G_Position to;
		try{
		from = diagram.getGUMLClass(relation.getFrom().get(0)).getPos();
		}
		catch (Exception e){
			System.out.println("name: " + relation.getFrom().get(0));
			from = new G_Position(0, 0);
		}
		try{
		to = diagram.getGUMLClass(relation.getTo()).getPos();
		}
		catch (Exception e){
			System.out.println("name: " + relation.getTo());
			to = new G_Position(0, 0);
		}

		root.setStartX(from.x);
		root.setStartY(from.y);
		root.setEndX(to.x);
		root.setEndY(to.y);
	}

	public Node getNode(){return root;}
}
