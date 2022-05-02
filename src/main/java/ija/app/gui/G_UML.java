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
	//private List<G_>
	private Scene classDiagramScene;
	private List<Scene> sequenceDiagramScenes;

	public G_UML(UML uml, Stage stage) throws IOException {
		this.uml = uml;
		this.stage = stage;

		classDiagram = new G_UMLClassDiagram(uml.getClassDiagram());
		createClassDiagramScene();
	}

	private void createClassDiagramScene() throws IOException {

		Pane root = FXMLLoader.load(getClass().getResource("fxml/G_ClassDiagramSceene.fxml"));
		classDiagramScene = new Scene((Parent) root);
		root.getChildren().add(classDiagram.getNode());

		setEventHandlers(root);
	}

	private void setEventHandlers(Node n) {
		n.lookup("#newRelation").setOnMouseClicked(e->{
			System.out.println("New relation");
		});

		n.lookup("#newClass").setOnMouseClicked(e->{
			System.out.println("New class");
		});

		n.lookup("#Edit").setOnMouseClicked(e->{
			System.out.println("Edit");
		});
	}

	public Scene getMainScene(){
		return classDiagramScene;
	}

}
