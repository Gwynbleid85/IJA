package ija.app.gui;

import com.sun.javafx.scene.SceneEventDispatcher;
import ija.app.uml.UML;
import ija.app.uml.classDiagram.UMLClass;
import ija.app.uml.classDiagram.UMLClassDiagram;
import ija.app.uml.sequenceDiagram.UMLSequenceDiagram;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class G_UML {

	private Stage stage;
	private UML uml;
	private G_UMLClassDiagram classDiagram;
	private List<G_UMLSequenceDiagram> sequenceDiagrams;

	/**
	 * Constructor of graphical representation of whole UML
	 * @param uml UML to be represented
	 * @param stage Stage on application
	 * @throws IOException
	 */
	public G_UML(UML uml, Stage stage) throws IOException {
		this.uml = uml;
		this.stage = stage;

		classDiagram = new G_UMLClassDiagram(uml.getClassDiagram(), this);

		sequenceDiagrams = new ArrayList<>();
		for (UMLSequenceDiagram sd: uml.getSequenceDiagrams()){
			sequenceDiagrams.add(new G_UMLSequenceDiagram(sd, this)); //add all sequence diagrams into list
		}
	}


	/**
	 * Get main scene of application (classDiagramScene)
	 * @return main scene of application (classDiagramScene)
	 */
	public Scene getMainScene(){
		return sequenceDiagrams.get(0).getScene();
	}

	/**
	 * Set selected scene
	 * @param type Type of scene (UMLClassDiagram / UMLSequenceDiagram)
	 * @param name If type == UMLSequenceDiagram -> name of UMLSequenceDiagram
	 */
	public void setScene(int type, String name){
		//TODO: set right size (???stage.getScene().getSize()???)
		if(type == 0)
			stage.setScene(classDiagram.getScene());

		stage.show();
	}


	public void draw() throws IOException {
		classDiagram.draw(stage);
		for (G_UMLSequenceDiagram gsd : sequenceDiagrams){
			gsd.draw(stage);
		}
	}

	public Stage getStage(){
		return stage;
	}


	public UML getUml(){
		return uml;
	}

	public G_UMLClassDiagram getClassDiagram(){
		return classDiagram;
	}
}
