package ija.app;

import ija.app.gui.G_UML;
import ija.app.gui.G_UMLClass;
import ija.app.gui.G_UMLClassDiagram;
import ija.app.uml.UML;
import ija.app.uml.classDiagram.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * Class for starting GUI, just a try, not part of Homework3!
 */
public class App extends Application
{
	final FileChooser fileChooser = new FileChooser();
	public static void main(String[] args) {
		launch(args);
	}

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("UML diagrams");


	    Pane root  = FXMLLoader.load(getClass().getResource("fxml/startScreen.fxml"));
		root.lookup("#Load").setOnMouseClicked(e -> {
			File file = fileChooser.showOpenDialog(stage);
			UML uml = null;
			if (file != null) {
				uml = UML.loadDiagramsFromFile(file.getAbsolutePath());
			}
			G_UML gUml;
			try {
				gUml = new G_UML(uml, stage);
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
			stage.setScene(gUml.getMainScene());
		});

	    root.lookup("#New").setOnMouseClicked(e -> {
		    UML uml = new UML();
		    G_UML gUml;
		    try {
			    gUml = new G_UML(uml, stage);
		    } catch (IOException ex) {
			    throw new RuntimeException(ex);
		    }
		    stage.setScene(gUml.getMainScene());
	    });


	    stage.setScene(new Scene(root, 500, 500));
		stage.show();
    }
}
