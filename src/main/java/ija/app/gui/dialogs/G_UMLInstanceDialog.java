package ija.app.gui.dialogs;

import ija.app.gui.G_UMLClassInstance;
import ija.app.gui.G_UMLSequenceDiagram;
import ija.app.uml.classDiagram.UMLClass;
import ija.app.uml.sequenceDiagram.UMLClassInstance;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

public class G_UMLInstanceDialog {

    private UMLClassInstance umlInstance;
    private G_UMLSequenceDiagram sequenceDiagram;
    private VBox template;
    private Stage stage;
    /** True if creating new Isntance, false if editing instance*/
    private boolean creatingNew;
    /**Signalize change when editing */


    /**
     * Constructor of Dialog for creating Instance
     * @param newInstance
     * @param sd          GUI sequence diagram
     */
    public G_UMLInstanceDialog(UMLClassInstance newInstance, G_UMLSequenceDiagram sd, boolean creatingNew) {
        umlInstance = newInstance;
        this.sequenceDiagram = sd;
        this.creatingNew = creatingNew;
    }

    /**
     * Method which creates scene for Instance Dialog
     * @return scene
     * @throws IOException
     */
    private Scene createScene() throws IOException {
        if(creatingNew) {
            VBox template = FXMLLoader.load(getClass().getResource("fxml/AddInstance.fxml"));
            this.template = template;
        }
        else {
            VBox template = FXMLLoader.load(getClass().getResource("fxml/editInstance.fxml"));
            this.template = template;
        }

        setEventHandlers();

        //if creating new class
        if(creatingNew){
            ((TextField)template.lookup("#instanceName")).setText("new"); //set instance name for the first usage
            Set<UMLClass> allClasses =  umlInstance.getClassDiagram().getClasses();
            ComboBox comboBox = (ComboBox)template.lookup("#classCombobox");
            comboBox.setVisibleRowCount(4); //max visible classes
            comboBox.setValue(allClasses.stream().findFirst().get().getName()); //implicitly show the name of first class

            for (UMLClass c: allClasses){
                comboBox.getItems().add(c.getName()); //add all classes to combobox
            }
        }
        else { //if editing the Instance
            loadData();
        }
        return new Scene(template);
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

    /**
     * Function which loads data for editing Instance
     */
    private void loadData() {
        /* Set class name */
        ((Label)template.lookup("#classLabel")).setText(umlInstance.getClassName());
        /*Set instance name */
        ((TextField)template.lookup("#instanceName")).setText(umlInstance.getName());
    }


    /**
     * Method to save data of new Instance
     */
    private void saveData(){
        //save Instance name
        umlInstance.setName(((TextField) template.lookup("#instanceName")).getText());
        if(creatingNew){
            /* Save class name*/
            umlInstance.setClassName((String) ((ComboBox)template.lookup("#classCombobox")).getSelectionModel().getSelectedItem());
        }
    }

    /**
     * Method which checks if same Instance doesnt already exist
     * @return true if OK
     */
    private boolean checkData() {
        //Check if Instance doesn't already exist
        String instanceName = (((TextField) template.lookup("#instanceName")).getText());
        String className;
        if(creatingNew){
            className = (String)(((ComboBox)template.lookup("#classCombobox")).getSelectionModel().getSelectedItem());
        }
        else {
            className = (String)(((Label)template.lookup("#classLabel")).getText());
        }
        String wholeName = instanceName + ":" + className;


        for (UMLClassInstance i: sequenceDiagram.getDiagram().getInstances())
            if(Objects.equals(i.getId(), wholeName)){
                setErr("Instance already exists!");
                return false;
            }
        return true;
    }

    /**
     * Method which sets event handlers of buttons in dialog
     */
    private void setEventHandlers(){
        //Clicking the save button
        ((Button)template.lookup("#SaveButton")).setOnMouseClicked(e ->{
            if(e.isStillSincePress()){
                //saving when adding new
                if(creatingNew){
                    if(checkData()) { //check if the inputed data is valid
                        saveData();
                        stage.close();
                    }
                }
                else { //saving when editing
                    /* if instance was changed*/
                    String oldName = umlInstance.getId();
                    String newName = (((TextField) template.lookup("#instanceName")).getText()) + ":" + (((Label)template.lookup("#classLabel")).getText());
                    if(!Objects.equals(umlInstance.getName(), ((TextField) template.lookup("#instanceName")).getText())){
                        if(checkData()) { //check if the inputed data is valid
                            saveData();
                            sequenceDiagram.updatedInstanceName(oldName, newName);
                            stage.close();
                        }
                    }
                    else{ //if instance wasn't changed
                        stage.close();
                    }
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
     * Set error on Dialog
     * @param err Error message to be set
     */
    private void setErr(String err){
        ((Label)template.lookup("#Error")).setText(err);
    }
}
