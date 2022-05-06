package ija.app.gui.dialogs;

import ija.app.gui.G_UML;
import ija.app.uml.UML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class G_LoadFile {
	private Stage stage;
	private G_UML parent;
	private VBox root;
	private UML newUML;

	public G_LoadFile(Stage stage, G_UML parent) throws IOException {
		this.parent = parent;
		this.stage = stage;
		root = FXMLLoader.load(getClass().getResource("fxml/LoadFile.fxml"));
		setEventHandlers();
	}

	private void setEventHandlers() {
		/* Create button */
		root.lookup("#Yes").setOnMouseClicked( e -> {
			FileChooser fileChooser = new FileChooser();
			File file = fileChooser.showOpenDialog(stage);
			if(file != null)
				newUML = UML.loadDiagramsFromFile(file.getAbsolutePath());
			stage.close();
		});

		/* Cancel button */
		root.lookup("#No").setOnMouseClicked( e -> {
			newUML = null;
			stage.close();
		});

	}

	public UML showDialog(){
		Stage stage = new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(this.stage);
		stage.setScene(new Scene(root));
		this.stage = stage;
		stage.showAndWait();
		return newUML;
	}
}
