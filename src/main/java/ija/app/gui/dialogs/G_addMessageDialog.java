package ija.app.gui.dialogs;

import ija.app.gui.G_UMLSequenceDiagram;
import ija.app.uml.classDiagram.UMLClassDiagram;
import ija.app.uml.classDiagram.UMLClassMethod;
import ija.app.uml.sequenceDiagram.UMLClassInstance;
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

public class G_addMessageDialog {
    private UMLClassDiagram classDiagram;
    private UMLMessage umlMessage;
    private G_UMLSequenceDiagram sequenceDiagram;
    private VBox template;
    private boolean isReturnMessage;
    private Stage stage;
    public boolean isChanged;

    public G_addMessageDialog(UMLClassDiagram cd, UMLMessage umlMessage, G_UMLSequenceDiagram sd, boolean isReturnMessage) {
        classDiagram = cd;
        this.umlMessage = umlMessage;
        this.sequenceDiagram = sd;
        this.isReturnMessage = isReturnMessage;
        this.isChanged = false;
    }

    private Scene createScene() throws IOException {
        VBox template;
        //if scene for edit return message
        if(isReturnMessage) {
            template = FXMLLoader.load(getClass().getResource("fxml/addReturnMessage.fxml"));
        }
        else {
            template = FXMLLoader.load(getClass().getResource("fxml/addMessage.fxml"));
        }
        this.template = template;
        setEventHandlers();
        setDialogData();
        return new Scene(template);
    }

    private void setDialogData() {

        /*Set From instances */
        List<UMLClassInstance> allInstances = sequenceDiagram.getDiagram().getInstances();
        ComboBox fromComboBox = (ComboBox)template.lookup("#fromCombobox");
        fromComboBox.setVisibleRowCount(4); //max visible classes
        for(UMLClassInstance ci: allInstances){
            fromComboBox.getItems().add(ci.getId()); //add all instances to combobox
        }
        /*Set To instances */
        ComboBox toComboBox = (ComboBox)template.lookup("#toCombobox");
        toComboBox.setVisibleRowCount(4); //max visible classes
        for(UMLClassInstance ci: allInstances){
            toComboBox.getItems().add(ci.getId()); //add all instances to combobox
        }

        fromComboBox.setValue(allInstances.stream().findAny().get().getId());
        toComboBox.setValue(allInstances.stream().findAny().get().getId());


        //if is a return message
        if(isReturnMessage){
            /*Set initial message */
            ((TextField)template.lookup("#messageText")).setText("Ok");
        }
        else{
            ((ComboBox) template.lookup("#messageCombobox")).setVisibleRowCount(4);
        }
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
                    isChanged = true;
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
        //Clicking the message combobox
        if(!isReturnMessage){
            ComboBox messageCombobox = ((ComboBox)template.lookup("#messageCombobox"));

            messageCombobox.setOnMouseClicked(e ->{
                //clear combobox
                messageCombobox.getSelectionModel().clearSelection();
                messageCombobox.getItems().clear();
                //get the 'to' class
                String toInstance = (String)((ComboBox)template.lookup("#toCombobox")).getSelectionModel().getSelectedItem();

                //todo when no
                String []splitted = toInstance.split(":");
                String className;
                try{
                    className = splitted[1];
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }


                List<UMLClassMethod> ownMethods= classDiagram.getUMLClassOwnMethods(className);
                List<UMLClassMethod> inheritedMethods = classDiagram.getUMLClassInheritedMethods(className);
                if(inheritedMethods == null){
                    inheritedMethods = new ArrayList<>();
                }
                List<UMLClassMethod> allMethods = new ArrayList<>();
                allMethods.addAll(ownMethods);
                allMethods.addAll(inheritedMethods);

                //add methods (messages) into combobox)
                for(UMLClassMethod cm: allMethods){
                    ((ComboBox)template.lookup("#messageCombobox")).getItems().add(cm.getName()); //add all instances to combobox
                }

            });
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
        umlMessage.setFrom((String)((ComboBox)template.lookup("#fromCombobox")).getSelectionModel().getSelectedItem());
        umlMessage.setTo((String)((ComboBox)template.lookup("#toCombobox")).getSelectionModel().getSelectedItem());
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
        else{

            String fromInstance = (String) ((ComboBox)template.lookup("#fromCombobox")).getSelectionModel().getSelectedItem();
            String toInstance = (String) ((ComboBox)template.lookup("#toCombobox")).getSelectionModel().getSelectedItem();
            String message = (String) ((ComboBox)template.lookup("#messageCombobox")).getSelectionModel().getSelectedItem();

            if(Objects.equals(fromInstance, null) || Objects.equals(toInstance, null) || Objects.equals(message, null)){
                setErr("Fill all boxes!");
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
    public boolean showDialog(Stage parent) throws IOException {
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(parent);
        stage.setScene(createScene());
        this.stage = stage;
        stage.showAndWait();
        return isChanged;
    }
}
