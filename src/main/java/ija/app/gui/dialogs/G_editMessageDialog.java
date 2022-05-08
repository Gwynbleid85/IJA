package ija.app.gui.dialogs;

import ija.app.gui.G_UMLSequenceDiagram;
import ija.app.uml.classDiagram.UMLClassDiagram;
import ija.app.uml.classDiagram.UMLClassMethod;
import ija.app.uml.sequenceDiagram.UMLMessage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * @author Jiri Mladek (xmlade01)
 * Class representing GUI dialog for editing messsages
 */
public class G_editMessageDialog {
    private UMLClassDiagram classDiagram;
    private UMLMessage umlMessage;
    private G_UMLSequenceDiagram sequenceDiagram;
    private VBox template;
    private boolean isReturnMessage;
    private Stage stage;

    public G_editMessageDialog(UMLClassDiagram cd, UMLMessage umlMessage, G_UMLSequenceDiagram sd, boolean isReturnMessage) {
        classDiagram = cd;
        this.umlMessage = umlMessage;
        this.sequenceDiagram = sd;
        this.isReturnMessage = isReturnMessage;
    }

    private Scene createScene() throws IOException{
        VBox template;
        //if scene for edit return message
        if(isReturnMessage) {
            template = FXMLLoader.load(getClass().getResource("fxml/editReturnMessage.fxml"));
        }
        else {
            template = FXMLLoader.load(getClass().getResource("fxml/editMessage.fxml"));
        }
        this.template = template;
        setEventHandlers();
        loadData();
        return new Scene(template);

    }

    /**
     * Method which sets event handlers of buttons in dialog
     */
    private void setEventHandlers(){
        //Clicking the save button
        ((Button)template.lookup("#SaveButton")).setOnMouseClicked(e ->{
            if(e.isStillSincePress()){
                if(checkData()){
                    saveData();
                    stage.close();
                }
            }
        });
        //Clicking the Cancel button
        ((Button)template.lookup("#CancelButton")).setOnMouseClicked(e ->{
            if(e.isStillSincePress()){
                stage.close();
            }
        });
    }

    /**
     * Function which loads data for editing Message
     */
    private void loadData() {
        /* Set From instance*/
        ((Label)template.lookup("#fromLabel")).setText(umlMessage.getFrom());
        /*Set To instance */
        ((Label)template.lookup("#toLabel")).setText(umlMessage.getTo());

        /*Set message */
        if(isReturnMessage){
            ((TextField)template.lookup("#messageText")).setText(umlMessage.getMessage());
        }
        /*If is a normal message */
        else {
            //get the class of From instance
            List<UMLClassMethod> ownMethods= classDiagram.getUMLClassOwnMethods(umlMessage.getClassTo());
            List<UMLClassMethod> inheritedMethods = classDiagram.getUMLClassInheritedMethods(umlMessage.getClassTo());
            if(inheritedMethods == null){
                inheritedMethods = new ArrayList<>();
            }
            ComboBox comboBox = (ComboBox)template.lookup("#messageCombobox");
            comboBox.setVisibleRowCount(4); //max visible classes
            for (UMLClassMethod c: ownMethods){
                comboBox.getItems().add(c.getName()); //add all methods to combobox
            }
            for (UMLClassMethod c: inheritedMethods){
                comboBox.getItems().add(c.getName()); //add all methods to combobox
            }
            comboBox.setValue(umlMessage.getMessage());
        }
    }


    /**
     * Method to save data of message
     */
    private void saveData() {
        //save Message
        if (isReturnMessage) {
            umlMessage.setMessage(((TextField) template.lookup("#messageText")).getText());
        } else {
            umlMessage.setMessage((String)((ComboBox)template.lookup("#messageCombobox")).getSelectionModel().getSelectedItem());
        }
    }


    /**
     * Method which checks the changed data
     * @return true if OK
     */
    private boolean checkData() {
        if(isReturnMessage){
            String newMessageText = ((TextField) template.lookup("#messageText")).getText();
            if(Objects.equals(newMessageText, "")){
                setErr("Message can not be empty");
                return false;
            }
        }
        return true;
    }

    /**
     * Set error on Dialog
     * @param err Error message to be set
     */
    private void setErr(String err){
        ((Label)template.lookup("#Error")).setText(err);
    }



    /**
     * Class that created modal window
     * @param parent Parent of modal window
     * @return true if class was changed
     * @throws IOException
     */
    public void showDialog(Stage parent) throws IOException {
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(parent);
        stage.setScene(createScene());
        this.stage = stage;
        stage.showAndWait();
    }
}
