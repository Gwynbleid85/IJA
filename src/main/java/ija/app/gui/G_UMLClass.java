package ija.app.gui;

import ija.app.uml.classDiagram.UMLClass;
import ija.app.uml.classDiagram.UMLClassAttribute;
import ija.app.uml.classDiagram.UMLClassMethod;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class G_UMLClass {
	private G_UMLClassDiagram diagram;
	private UMLClass umlClass;
	private VBox GumlClass;



	public G_UMLClass(UMLClass c, G_UMLClassDiagram d) throws IOException {
		umlClass = c;
		diagram = d;
		GumlClass = FXMLLoader.load(getClass().getResource("fxml/G_UMLClass.fxml"));

		setEventHandlers();
		updateText();
	}

	private void setEventHandlers() {

		/* Dragging UMLClass*/
		GumlClass.setOnMouseDragged(e -> {
			if(!e.isStillSincePress()){
				GumlClass.setLayoutX(e.getSceneX() - GumlClass.getWidth()/2);
				GumlClass.setLayoutY(e.getSceneY() - GumlClass.getHeight()/2);
				diagram.updateRelations();
			}
		});

		/* Selecting UMLClass*/
		GumlClass.setOnMouseClicked(e -> {
			if(e.isStillSincePress()){
				GumlClass.setStyle("-fx-background-color : lightgray; -fx-border-color : black; -fx-border-width : 2");
			}
		});
	}

	public Node getNode(){
		return GumlClass;
	}
	public String getName() { return umlClass.getName(); }
	private void updateText() {
		// Update name
		((Label)GumlClass.lookup("#umlClassName")).setText(umlClass.getName());

		//Update attributes
		VBox attribs = (VBox) GumlClass.lookup("#umlClassAttribs");
		attribs.getChildren().clear();
		for(UMLClassAttribute a : umlClass.getAttributes()){
			Label att = new Label(a.toString());
			//Todo set css
			//att.setFont(new Font(14));
			attribs.getChildren().add(att);
		}

		//Update methods
		VBox methods = (VBox) GumlClass.lookup("#umlClassMethods");
		methods.getChildren().clear();
		for(UMLClassMethod a : umlClass.getMethods()){
			Label met = new Label(a.toString());
			//Todo set css
			//met.setFont(new Font(14));
			methods.getChildren().add(met);
		}
	}

	public G_Position getPos(){
		return new G_Position(GumlClass.getLayoutX() +GumlClass.getWidth()/2, GumlClass.getLayoutY() + GumlClass.getHeight()/2);
	}



}
