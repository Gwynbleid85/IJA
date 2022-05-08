package ija.app.gui.dialogs;

import ija.app.gui.G_UMLClassDiagram;
import ija.app.uml.classDiagram.UMLClass;
import ija.app.uml.classDiagram.UMLClassAttribute;
import ija.app.uml.classDiagram.UMLClassMethod;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

/**
 * @author Milos Hegr (xhegrm00)
 */
public class G_EditUMLClassDialog {

	private UMLClass umlClass;
	private G_UMLClassDiagram classDiagram;
	private Pane template;
	private Stage stage;
	private  boolean isNew;

	private boolean isChanged;

	public G_EditUMLClassDialog(UMLClass umlClass, G_UMLClassDiagram classDiagram, boolean isNew) {
		this.umlClass = umlClass;
		this.classDiagram = classDiagram;
		this.isNew = isNew;
		this.isChanged = false;
	}

	private Scene createScene() throws IOException {
		Pane template = FXMLLoader.load(getClass().getResource("fxml/EditClass.fxml"));
		this.template = template;
		setEventHandlers();
		if(isNew)
			((TextField)template.lookup("#ClassName")).setText(umlClass.getName());
		else
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

	private void loadData() throws IOException {
		/* Set class name */
		((TextField)template.lookup("#ClassName")).setText(umlClass.getName());
		/* Set isInterface */
		((CheckBox)template.lookup("#IsInterface")).selectedProperty().setValue(umlClass.isInterface());
		/* Set attributes */
		for(UMLClassAttribute attrib : umlClass.getAttributes())
			addAttribute(attrib.getAccessMod(), attrib.getDatatype(), attrib.getName());
		/* Set methods */
		for(UMLClassMethod method : umlClass.getMethods())
			addMethod(method.getAccessMod(), method.getName());
	}

	/**
	 * Check if new/edited class is OK
	 * @return true if new/edited class is OK, false otherwise
	 */
	private boolean checkData(){
		/* Check if className isn't empty */
		if(Objects.equals(((TextField) template.lookup("#ClassName")).getText(), "")){
			setErr("Class name can't be empty!");
			return false;
		}
		/* Check if name is unique */
		/* Only if creating new class */
		if(isNew)
			if(classDiagram.getGUMLClass(((TextField) template.lookup("#ClassName")).getText()) != null){
				setErr("Class with this name already exists!");
				return false;
			}
		/* Check if all UMLAttributes have name */
		for(Node attrib : template.lookupAll("#attribute")){
			/* Check if textField with name is empty */
			if(Objects.equals(((TextField) ((HBox) attrib).getChildren().get(2)).getText(), "")){
				setErr("Attribute name can't be empty!");
				return false;
			}
		}
		/* Check if all UMLMethods have name */
		for(Node method : template.lookupAll("#method")){
			/* Check if textField with name is empty */
			if(Objects.equals(((TextField) ((HBox) method).getChildren().get(1)).getText(), "")){
				setErr("Method name can't be empty!");
				return false;
			}
		}

		return true;
	}

	/* Save edited data to given UMLClass */
	private void saveData() {
		/* Save class name */
		umlClass.setName(((TextField) template.lookup("#ClassName")).getText());

		/* Save isInterface */
		umlClass.setIsInterface(((CheckBox) template.lookup("#IsInterface")).isSelected());
		((TextField) template.lookup("#ClassName")).setText(umlClass.getName());

		/* Delete old attributes*/
		umlClass.delAttributes();
		/* Save new attributes */
		for (Node GAttrib : template.lookupAll("#attribute")) {
			UMLClassAttribute newAttrib = new UMLClassAttribute(
					((TextField)((HBox)GAttrib).getChildren().get(2)).getText(),    //Name
					((TextField)((HBox)GAttrib).getChildren().get(1)).getText(),   //DataType
					(String)((ComboBox)((HBox)GAttrib).getChildren().get(0)).getSelectionModel().getSelectedItem() //AccessMod
			);
			umlClass.addAttribute(newAttrib);
		}

		/* delete old methods */
		umlClass.delMethods();
		/* Save new methods */
		for (Node GMethod : template.lookupAll("#method")) {
			UMLClassMethod newMethod = new UMLClassMethod(
					((TextField)((HBox)GMethod).getChildren().get(1)).getText(),    //Name
					(String)((ComboBox)((HBox)GMethod).getChildren().get(0)).getSelectionModel().getSelectedItem() //AccessMod
			);
			umlClass.addMethod(newMethod);
		}

	}

	/**
	 * Method to add new attribute option to dialog
	 * @throws IOException
	 */
	private void addAttribute() throws IOException {
		/* Load attribute template */
		HBox newAttribute = FXMLLoader.load(getClass().getResource("fxml/Attribute.fxml"));
		/* Setup ComboBox */
		((ComboBox)newAttribute.lookup("#accessMod")).setItems(FXCollections.observableArrayList("+" , "-" , "#" , "~"));
		((ComboBox)newAttribute.lookup("#accessMod")).getSelectionModel().selectFirst();
		/* Setup delete Button */
		((Button)newAttribute.lookup("#delButton")).setOnMouseClicked( e -> {
			if(e.isStillSincePress()){
				((VBox)template.lookup("#AttributesBox")).getChildren().remove(((Button)e.getSource()).getParent());
			}
		});
		/* Add attribute */
		((VBox)template.lookup("#AttributesBox")).getChildren().add(newAttribute);
	}

	/**
	 * Method to add new attribute option to dialog
	 * @throws IOException
	 */
	private void addAttribute(String accessMod, String dataType, String name) throws IOException {
		/* Load attribute template */
		HBox newAttribute = FXMLLoader.load(getClass().getResource("fxml/Attribute.fxml"));
		/* Set ComboBox */
		((ComboBox)newAttribute.lookup("#accessMod")).setItems(FXCollections.observableArrayList("+" , "-" , "#" , "~"));
		((ComboBox)newAttribute.lookup("#accessMod")).getSelectionModel().select(accessMod);
		/* Set datatype */
		((TextField)newAttribute.lookup("#datatype")).setText(dataType);
		/* Set name*/
		((TextField)newAttribute.lookup("#name")).setText(name);
		/* Setup delete Button */
		newAttribute.lookup("#delButton").setOnMouseClicked(e -> {
			if(e.isStillSincePress()){
				((VBox)template.lookup("#AttributesBox")).getChildren().remove(((Button)e.getSource()).getParent());
			}
		});
		/* Add attribute */
		((VBox)((ScrollPane)((VBox)template.getChildren().get(4)).getChildren().get(2)).getContent()).getChildren().add(newAttribute);
	}

	/**
	 * Method to add new method option to dialog
	 * @throws IOException
	 */
	private void addMethod() throws IOException {
		/* Load attribute template */
		HBox newMethod = FXMLLoader.load(getClass().getResource("fxml/Method.fxml"));
		/* Setup ComboBox */
		((ComboBox)newMethod.lookup("#accessMod")).setItems(FXCollections.observableArrayList("+" , "-" , "#" , "~"));
		((ComboBox)newMethod.lookup("#accessMod")).getSelectionModel().selectFirst();
		/* Setup delete Button */
		((Button)newMethod.lookup("#delButton")).setOnMouseClicked( e -> {
			if(e.isStillSincePress()){
				((VBox)template.lookup("#MethodsBox")).getChildren().remove(((Button)e.getSource()).getParent());
			}
		});
		/* Add attribute */
		((VBox)template.lookup("#MethodsBox")).getChildren().add(newMethod);
	}

	/**
	 * Method to add new method option to dialog
	 * @throws IOException
	 */
	private void addMethod(String accessMod, String name) throws IOException {
		/* Load attribute template */
		HBox newMethod = FXMLLoader.load(getClass().getResource("fxml/Method.fxml"));
		/* Set ComboBox */
		((ComboBox)newMethod.lookup("#accessMod")).setItems(FXCollections.observableArrayList("+" , "-" , "#" , "~"));
		((ComboBox)newMethod.lookup("#accessMod")).getSelectionModel().select(accessMod);
		/* Set name */
		((TextField)newMethod.lookup("#name")).setText(name);
		/* Setup delete Button */
		((Button)newMethod.lookup("#delButton")).setOnMouseClicked( e -> {
			if(e.isStillSincePress()){
				((VBox)template.lookup("#MethodsBox")).getChildren().remove(((Button)e.getSource()).getParent());
			}
		});
		/* Add attribute */
		(((VBox)((ScrollPane)((VBox)template.getChildren().get(5)).getChildren().get(2)).getContent())).getChildren().add(newMethod);
	}
	private void setEventHandlers(){
		/* Save button clicked */
		((Button)template.lookup("#SaveButton")).setOnMouseClicked( e ->{
			if(e.isStillSincePress()){
				if(checkData()){
					saveData();
					isChanged = true;
					stage.close();
				}
			}
		});

		/* Cancel button clicked */
		((Button)template.lookup("#CancelButton")).setOnMouseClicked(e ->{
			if(e.isStillSincePress()){
				stage.close();
			}
		});

		/* Add new attribute button */
		((Button)template.lookup("#AddAttribute")).setOnMouseClicked(e ->{
			if(e.isStillSincePress()){
				try {
					addAttribute();
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			}
		});

		/* Add new attribute button */
		((Button)template.lookup("#AddMethod")).setOnMouseClicked(e ->{
			if(e.isStillSincePress()){
				try {
					addMethod();
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			}
		});


	}

	/**
	 * Set error on Dialog
	 * @param err Error message to be set
	 */
	private void setErr(String err){
		((Label)template.lookup("#Error")).setText(err);
	}






}
