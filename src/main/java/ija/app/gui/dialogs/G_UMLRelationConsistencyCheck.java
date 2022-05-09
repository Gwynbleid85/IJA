package ija.app.gui.dialogs;

import ija.app.gui.G_UMLClassDiagram;
import ija.app.uml.classDiagram.UMLClass;
import ija.app.uml.classDiagram.UMLRelation;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Milos Hegr (xhegrm00)
 */
public class G_UMLRelationConsistencyCheck {
	private Stage stage;
	private G_UMLClassDiagram diagram;

	public G_UMLRelationConsistencyCheck(G_UMLClassDiagram diagram, Stage stage){
		this.stage = stage;
		this.diagram = diagram;
	}
	public boolean checkConsistency(UMLRelation relation)  throws IOException {
		if(!relation.consistencyCheck(diagram.getClasses())){
			/* Check to */
			if(!diagram.getClasses().contains(diagram.getDiagram().getClassByName(relation.getTo()))){
				if(consistencyFixPopup(relation.getName(), relation.getTo()))
					return false;
			}
			/* Check from */
			if(!diagram.getClasses().contains(diagram.getDiagram().getClassByName(relation.getFrom()))){
				if(consistencyFixPopup(relation.getName(), relation.getFrom()))
					return false;
			}
		}
		return true;
	}

	private boolean consistencyFixPopup(String relName, String className) throws IOException {
		AtomicBoolean toDelete = new AtomicBoolean(false);
		VBox root = FXMLLoader.load(getClass().getResource("fxml/consistencyFixPopup.fxml"));
		/* Set info messages */
		((Label)root.lookup("#title")).setText("Unconsistency in relation \"" + relName + "\"");
		((Label)root.lookup("#err")).setText("Do you want to delete this relation\nor\ncreate new class " + className);
		Stage stage = new Stage();
		/* Set buttons */
		((Button)root.lookup("#create")).setOnMouseClicked(e -> {
			try {
				diagram.addUMLClass(new UMLClass(className));
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
			stage.close();
		});

		((Button)root.lookup("#delete")).setOnMouseClicked( e -> {

			toDelete.set(true);
			stage.close();
		});

		stage.setScene(new Scene(root));
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(this.stage);

		stage.showAndWait();
		return toDelete.get();
	}

}
