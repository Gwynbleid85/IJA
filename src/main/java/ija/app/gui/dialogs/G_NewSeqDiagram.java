package ija.app.gui.dialogs;

import ija.app.gui.G_UML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.util.Objects;

public class G_NewSeqDiagram {

	private Stage stage;
	private G_UML parent;
	private VBox root;
	private String newName;

	public G_NewSeqDiagram(Stage stage, G_UML parent) throws IOException {
		this.parent = parent;
		this.stage = stage;
		root = FXMLLoader.load(getClass().getResource("fxml/NewSequenceDiagram.fxml"));
		setEventHandlers();
	}

	private void setEventHandlers() {
		/* Create button */
		root.lookup("#Create").setOnMouseClicked( e -> {
			 newName = ((TextField)root.lookup("#NewName")).getText();
			Label err = ((Label)root.lookup("#Err"));
			if(Objects.equals(newName, "")){
				err.setText("Diagram name cannot be empty!");
			}
			else{
				try {
					if(!parent.createNewSeqDiagram(newName)){
						err.setText("Sequence diagram " + newName + " already exists");
					}
					else{
						stage.close();
					}
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			}
		});

		/* Cancel button */
		root.lookup("#Cancel").setOnMouseClicked( e -> {
			newName = null;
			stage.close();
		});

	}

	public String showDialog(){
		Stage stage = new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(this.stage);
		stage.setScene(new Scene(root));
		this.stage = stage;
		stage.showAndWait();
		return newName;
	}
}
