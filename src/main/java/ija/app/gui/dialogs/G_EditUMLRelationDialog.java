package ija.app.gui.dialogs;

import ija.app.gui.G_UMLClassDiagram;
import ija.app.uml.classDiagram.UMLClass;
import ija.app.uml.classDiagram.UMLRelation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class G_EditUMLRelationDialog {

	private UMLRelation umlRelation;
	private G_UMLClassDiagram classDiagram;
	private Pane template;
	private Stage stage;
	private boolean isNew;
	private boolean isChanged;

	private ObservableList<String> RelTypes =  FXCollections.observableArrayList("Association", "Aggregation", "Composition", "Generalization");
	private ObservableList<String> Cardinalities =  FXCollections.observableArrayList("1", "0..1", "*", "1..*", "None");

	public G_EditUMLRelationDialog(UMLRelation umlRelation, G_UMLClassDiagram classDiagram, boolean isNew){
		this.umlRelation = umlRelation;
		this.classDiagram = classDiagram;
		this.isNew = isNew;
		this.isChanged = false;
	}

	private Scene createScene() throws IOException {
		Pane template = FXMLLoader.load(getClass().getResource("fxml/EditRelation.fxml"));
		this.template = template;
		setEventHandlers();
		loadData();

		return new Scene(template);
	}

	/**
	 * Method that created modal window
	 * @param parent Parent of modal window
	 * @return true if class was changed
	 * @throws IOException
	 */
	public boolean showDialog(Stage parent) throws IOException {
		Stage stage = new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(parent);
		stage.setScene(createScene());
		this.stage = stage;
		stage.showAndWait();
		return isChanged;
	}

	private void setEventHandlers() {
		/* Save button clicked */
		template.lookup("#SaveButton").setOnMouseClicked( e -> {
			if(e.isStillSincePress()){
				saveData();
				isChanged = true;
				stage.close();
			}
		});

		/* Cancel button clicked */
		template.lookup("#CancelButton").setOnMouseClicked( e -> {
			if(e.isStillSincePress()){
				stage.close();
			}
		});

		/* Set class selections on From */
		((ComboBox)template.lookup("#ToName")).setOnAction( e ->{
			if(((ComboBox)template.lookup("#FromName")).getValue() == ((ComboBox)template.lookup("#ToName")).getValue())
				((ComboBox)template.lookup("#FromName")).getSelectionModel().clearSelection();

		});

		/* Set class selections on To */
		((ComboBox)template.lookup("#FromName")).setOnAction( e ->{
			if(((ComboBox)template.lookup("#FromName")).getValue() == ((ComboBox)template.lookup("#ToName")).getValue())
				((ComboBox)template.lookup("#ToName")).getSelectionModel().clearSelection();
		});
	}

	private void loadData(){
		/* Setup name*/
		((TextField)template.lookup("#RelName")).setText(umlRelation.getName());
		/* Setup Type comboBox*/
		((ComboBox)template.lookup("#RelType")).setItems(RelTypes);
		((ComboBox)template.lookup("#RelType")).getSelectionModel().selectFirst();
		/* Setup To card comboBox */
		((ComboBox)template.lookup("#ToCar")).setItems(Cardinalities);
		((ComboBox)template.lookup("#ToCar")).getSelectionModel().selectFirst();
		/* Setup  To classnames combobox */
		((ComboBox)template.lookup("#ToName")).setItems(getClassNames(""));
		((ComboBox)template.lookup("#ToName")).getSelectionModel().selectFirst();
		/*  Setup From card comboBox */
		((ComboBox)template.lookup("#FromCar")).setItems(Cardinalities);
		((ComboBox)template.lookup("#FromCar")).getSelectionModel().selectFirst();
		/* Setup  From classnames combobox */
		((ComboBox)template.lookup("#FromName")).setItems(getClassNames(""));
		((ComboBox)template.lookup("#FromName")).getSelectionModel().selectLast();

		if(!isNew){
			((ComboBox)template.lookup("#RelType")).getSelectionModel().select(umlRelation.getType());
			((ComboBox)template.lookup("#ToCar")).getSelectionModel().select(umlRelation.getCardinalityTo());
			((ComboBox)template.lookup("#ToName")).getSelectionModel().select(umlRelation.getTo());
			((ComboBox)template.lookup("#FromCar")).getSelectionModel().select(umlRelation.getCardinalityFrom());
			((ComboBox)template.lookup("#FromName")).getSelectionModel().select(umlRelation.getFrom());
		}
	}

	private void saveData() {
		/* Save name */
		umlRelation.setName(((TextField)template.lookup("#RelName")).getText());
		/* Save type*/
		umlRelation.setType((String) ((ComboBox)template.lookup("#RelType")).getValue());
		/* Save To */
		umlRelation.setTo((String) ((ComboBox)template.lookup("#ToName")).getValue());
		umlRelation.setCardinalityTo((String) ((ComboBox)template.lookup("#ToCar")).getValue());
		/* Save From*/
		umlRelation.setFrom((String) ((ComboBox)template.lookup("#FromName")).getValue());
		umlRelation.setCardinalityFrom((String) ((ComboBox)template.lookup("#FromCar")).getValue());
	}

	private ObservableList<String> getClassNames(String remove){
		ObservableList<String> names =  FXCollections.observableArrayList();
		for(UMLClass c : classDiagram.getClasses()){
			if(!Objects.equals(c.getName(), remove))
				names.add(c.getName());
		}
		return names;
	}

}
