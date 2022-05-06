package ija.app.gui;

import ija.app.uml.sequenceDiagram.UMLClassInstance;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

import java.io.IOException;

public class G_UMLClassInstance implements G_selectable{
    private G_UMLSequenceDiagram parent;
    private UMLClassInstance umlInstance;
    private AnchorPane GumlInstance;

    /**
     * Constructor of GUI UMLClassInstance
     * @param ci UMLClassinstance
     * @param gsd GUI sequence diagram to which instance belongs
     * @throws IOException
     */
    public G_UMLClassInstance(UMLClassInstance ci, G_UMLSequenceDiagram gsd) throws IOException {
        umlInstance = ci;
        parent = gsd;
        GumlInstance = FXMLLoader.load(getClass().getResource("fxml/G_UMLClassInstance.fxml"));

        setEventHandlers();
        updateText();
    }

    /**
     * Set event handlers
     */
    private void setEventHandlers() {

        /* Dragging UMLClassInstance*/
        GumlInstance.lookup("#instanceLabel").setOnMouseDragged(e -> {
            if(!e.isStillSincePress()){
                GumlInstance.setLayoutX(e.getSceneX() - GumlInstance.getWidth()/2);
            }
            try{
                parent.updateMessages();
                parent.positionLifelines();
            }catch(IOException exception){
                throw new RuntimeException(exception);
            }
        });

        /* Selecting UMLClassInstance*/
        GumlInstance.lookup("#instanceLabel").setOnMouseClicked(e -> {
            if(e.isStillSincePress()){
                setSelect(true);
                e.consume(); // differenciate selecting Instance and canvas
            }
        });
    }


    /**
     * Method which updates name of Instance
     */
    public void updateText(){
        //update instance name
        ((Label)GumlInstance.lookup("#instanceLabel")).setText(umlInstance.getId());
        //update messages

    }



    /**
     * Method which gets position of the Instance //todo y axis, at start x axis not counting
     * @param m
     * @return
     */
    public G_Position getPos(G_UMLMessage m){

        return new G_Position(GumlInstance.getLayoutX() + GumlInstance.getWidth()/2, 100+ GumlInstance.getLayoutY() + parent.getMessageOrder(m) * 50);

    }






    public Node getNode() {
        return GumlInstance;
    }

    /**
     * Method which gets full name of Instance
     * @return name of instance
     */
    public String getName() { return umlInstance.getId(); }


    /**
     * Method which sets the Instance to be selected and sets style
     * @param selected boolean if should be or should not be selected
     */
    @Override
    public void setSelect(boolean selected) {
        if(selected){
            parent.setSelected(this);
            GumlInstance.lookup("#instanceLabel").setStyle( "-fx-background-color : lightgray; " +
                    "-fx-border-color : black; -fx-border-width : 2; ");
            GumlInstance.lookup("#lifeLine").setStyle("-fx-stroke-width: 2; -fx-stroke-dash-array: 10");

        }
        else{
            GumlInstance.lookup("#instanceLabel").setStyle( "-fx-background-color : white; -fx-border-color : black;" +
                    " -fx-border-width : 1 ;");
            GumlInstance.lookup("#lifeLine").setStyle("-fx-stroke-width: 1; -fx-stroke-dash-array: 10");
        }
    }

    /**
     * Method which gets the type of selected item
     * @return UMLClassInstance
     */
    @Override
    public String getType() {
        return "UMLClassInstance";
    }


    public UMLClassInstance getUmlInstance(){
       return umlInstance;
    }
}
