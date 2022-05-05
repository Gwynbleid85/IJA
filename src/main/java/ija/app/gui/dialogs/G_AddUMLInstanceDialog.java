package ija.app.gui.dialogs;

import ija.app.gui.G_UMLSequenceDiagram;
import ija.app.uml.classDiagram.UMLClass;
import ija.app.uml.classDiagram.UMLClassDiagram;
import ija.app.uml.sequenceDiagram.UMLClassInstance;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;

public class G_AddUMLInstanceDialog{

    private UMLClassInstance umlInstance;
    private G_UMLSequenceDiagram sequenceDiagram;
    private VBox template;
    private Stage stage;


    /**
     * Constructor of Dialog for creating Instance
     * @param newInstance
     * @param sd          GUI sequence diagram
     */
    public G_AddUMLInstanceDialog(UMLClassInstance newInstance, G_UMLSequenceDiagram sd) {
        umlInstance = newInstance;
        this.sequenceDiagram = sd;
    }

    /**
     * Method which creates scene for Instance Dialog
     * @return scene
     * @throws IOException
     */
    private Scene createScene() throws IOException {
        VBox template = FXMLLoader.load(getClass().getResource("fxml/AddInstance.fxml"));
        this.template = template;
        setEventHandlers();

        ((TextField)template.lookup("#instanceName")).setText("new"); //set instance name for the first usage
        Set<UMLClass> allClasses =  umlInstance.getClassDiagram().getClasses();
        ComboBox comboBox = (ComboBox)template.lookup("#classCombobox");
        comboBox.setVisibleRowCount(4); //max visible classes
        comboBox.setValue(allClasses.stream().findFirst().get().getName()); //implicitly show the name of first class

        for (UMLClass c: allClasses){
            comboBox.getItems().add(c.getName()); //add all classes to combobox
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
     * Method to save data of new Instance
     */
    private void saveData(){
        //save Instance name
        umlInstance.setName(((TextField) template.lookup("#instanceName")).getText());
        /* Save class name*/
        umlInstance.setClassName((String) ((ComboBox)template.lookup("#classCombobox")).getSelectionModel().getSelectedItem());
    }

    private boolean checkData() {
        //Check if Instance doesn't already exist
        String instanceName = (((TextField) template.lookup("#instanceName")).getText());
        String className = (String)(((ComboBox)template.lookup("#classCombobox")).getSelectionModel().getSelectedItem());
        String wholeName = instanceName + ":" + className;


        for (UMLClassInstance i: sequenceDiagram.getDiagram().getInstances())
            if(Objects.equals(i.getId(), wholeName)){
                setErr("Instance already exists!");
                return false;
            }
        return true;
    }

    private void setEventHandlers(){
        //Clicking the save button
        ((Button)template.lookup("#SaveButton")).setOnMouseClicked(e ->{
            if(e.isStillSincePress()){
                if(checkData()) { //check if the inputed data is valid
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
     * Set error on Dialog
     * @param err Error message to be set
     */
    private void setErr(String err){
        ((Label)template.lookup("#Error")).setText(err);
    }
}
