package ija.app.gui;

import com.sun.javafx.scene.SceneEventDispatcher;
import ija.app.uml.UML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class G_UML {

	private Stage stage;
	private UML uml;
	private G_UMLClassDiagram classDiagram;

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
	}


	/**
	 * Get main scene of application (classDiagramScene)
	 * @return main scene of application (classDiagramScene)
	 */
	public Scene getMainScene(){
		return classDiagram.getScene();
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

	public Stage getStage(){
		return stage;
	}
}
