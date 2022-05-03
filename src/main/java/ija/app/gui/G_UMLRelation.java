package ija.app.gui;

import ija.app.uml.classDiagram.UMLClass;
import ija.app.uml.classDiagram.UMLRelation;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class G_UMLRelation {
	private UMLRelation relation;
	private G_UMLClassDiagram diagram;
	private Line root;
	public G_UMLRelation(UMLRelation r, G_UMLClassDiagram d) throws IOException {
		relation = r;
		diagram = d;
		root = FXMLLoader.load(getClass().getResource("fxml/G_UMLRelation.fxml"));

		update();
		System.out.println("Pos updated");
	}

	public void update() throws IOException {
		//fixConsistency();
		G_Position from;
		G_Position to;
		try{
			from = diagram.getGUMLClass(relation.getFrom().get(0)).getPos();
			to = diagram.getGUMLClass(relation.getTo()).getPos();
		}
		catch (Exception e){
			from = new G_Position(0, 0);
			to = new G_Position(0, 0);
		}

		root.setStartX(from.x);
		root.setStartY(from.y);
		root.setEndX(to.x);
		root.setEndY(to.y);
	}

	public void fixConsistency() throws IOException {
		if(relation.consistencyCheck(diagram.getClasses())){
			/* Check to */
			if(diagram.getClasses().contains(new UMLClass(relation.getTo())))
				if(consistencyFixPopup(relation.getName(), relation.getTo(), false))
					return;
			for(String from : relation.getFrom()){
				if(diagram.getClasses().contains(new UMLClass(from))){
					if(consistencyFixPopup(relation.getName(), from, relation.getFrom().size() > 1))
						return;
				}
			}
		}
	}

	private boolean consistencyFixPopup(String relName, String className, Boolean canSurvive) throws IOException {
		System.out.println("Unconsistent!");
		AtomicBoolean deleted = new AtomicBoolean(false);
		VBox root = FXMLLoader.load(getClass().getResource("fxml/consistencyFixPopup.fxml"));
		/* Set info messages */
		((Label)root.lookup("#title")).setText("Unconsistency in relation " + relName);
		if(canSurvive)
			((Label)root.lookup("#err")).setText("Do you want to remove class " + className +" from relation\nor\ncrete this class?");
		else
			((Label)root.lookup("#err")).setText("Do you want to delete this relation\nor\ncreate new class " + className);
		Stage stage = new Stage();
		/* Set buttons */
		((Button)root.lookup("#create")).setOnMouseClicked( e -> {

		});

		((Button)root.lookup("#delete")).setOnMouseClicked( e -> {
			diagram.delRelation(relation, this.root);
			deleted.set(true);
			stage.close();
		});

		stage.setScene(new Scene(root));
		stage.initStyle(StageStyle.UNDECORATED);
		stage.showAndWait();
		return deleted.get();
	}
	public Node getNode(){return root;}
}
