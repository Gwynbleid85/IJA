package ija.app.gui;


import ija.app.gui.dialogs.G_UMLInstanceDialog;
import ija.app.uml.sequenceDiagram.UMLClassInstance;
import ija.app.uml.sequenceDiagram.UMLMessage;
import ija.app.uml.sequenceDiagram.UMLSequenceDiagram;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class G_UMLSequenceDiagram {
    private UMLSequenceDiagram diagram;
    private G_UML parent;
    private Scene sequenceDiagramScene;
    private List<G_UMLMessage>messageOrder;
    /** Graphical representation of all UMLInstances */
    private List<G_UMLClassInstance> instances;
    /** Graphical representation of all UMLMessages */
    private List<G_UMLMessage> messages;
    /** Selected element (G_UMLInstance or G_UMLMessage) */
    private G_selectable selected;
    /** Main node of Graphical Sequence diagram*/
    private Group root;



    /**
     * Constructor of Sequence diagram
     *
     * @param sd     UML sequence diagram
     * @param parent parent object (G_UML)
     * @throws IOException
     */
    public G_UMLSequenceDiagram(UMLSequenceDiagram sd, G_UML parent) throws IOException {
        diagram = sd;
        messageOrder = new ArrayList<>();
        this.parent = parent;
        selected = null;
        root = new Group();
        /* Create new Scene */
        AnchorPane template = FXMLLoader.load(getClass().getResource("fxml/G_SequenceDiagramScene.fxml"));
        template.getChildren().add(root);
        sequenceDiagramScene = new Scene(template);
        sequenceDiagramScene.getRoot().lookup("#Edit").setDisable(true);
        sequenceDiagramScene.getRoot().lookup("#Delete").setDisable(true);

        /* Create gui instances for the according diagram */
        instances = new ArrayList<>();
        for (UMLClassInstance inst : sd.getInstances()) {
            G_UMLClassInstance gInst = new G_UMLClassInstance(inst, this); //todo
            instances.add(gInst);
            Node x = gInst.getNode();
            x.setLayoutX(x.getLayoutX() + 100 + 200 * (instances.size() - 1));
            root.getChildren().add(x);


        }
    }

    public void draw(Stage stage) throws IOException{
        //todo consistency of messages

        //create gui Messages for the according diagram
        messages = new ArrayList<>();
        for (UMLMessage m: diagram.getMessages()){
            G_UMLMessage gm = new G_UMLMessage(m, this);
            messages.add(gm);
            messageOrder.add(gm); //for use of getting position
            root.getChildren().add(gm.getNode());
        }
        setEventHandlers();
        updateMessages();
        positionLifelines();
        /*Resize the lifeLine when resizing the window size */
        parent.getStage().heightProperty().addListener((obs, oldVal, newVal) -> {
            positionLifelines();
        });
    }

    public void positionLifelines(){
        for (G_UMLClassInstance gInst: instances){
            Line lifeLine = ((Line)gInst.getNode().lookup("#lifeLine"));
            lifeLine.setStartX(((AnchorPane)gInst.getNode()).getWidth()/2);
            lifeLine.setEndX(((AnchorPane)gInst.getNode()).getWidth()/2);
            lifeLine.setEndY(getNode().getScene().getHeight() - 50);
        }

    }



    /**
     * Set event handlers
     */
    private void setEventHandlers(){
        /*If the canvas is selected (no element is selected) */
        sequenceDiagramScene.getRoot().setOnMouseClicked(e -> {
            if(e.isStillSincePress()){
                System.out.println("Clear select"); //todo
                if(selected != null)
                    selected.setSelect(false);
                selected = null;
                sequenceDiagramScene.getRoot().lookup("#Edit").setDisable(true);
                sequenceDiagramScene.getRoot().lookup("#Delete").setDisable(true);
            }
        });


        /* New Instance button*/
        sequenceDiagramScene.getRoot().lookup("#newObject").setOnMouseClicked(e->{
            //create empty new Instance
            UMLClassInstance newInstance = new UMLClassInstance(parent.getUml().getClassDiagram());
            G_UMLInstanceDialog dialog = new G_UMLInstanceDialog(newInstance, this, true);

            /*Show the created dialog*/
            try{
                dialog.showDialog(parent.getStage());
            }catch(IOException ex){
                throw new RuntimeException(ex);
            }
            //todo only if save button clicked!!!
            /*Add the prepared Instance*/
            try{
                addUMLInstance(newInstance);
            }
            catch (IOException ex){
                throw new RuntimeException(ex);
            }
            positionLifelines();
        });

        /*Edit button */
        sequenceDiagramScene.getRoot().lookup("#Edit").setOnMouseClicked(e->{
            if(Objects.equals(selected.getType(), "UMLClassInstance")){
                G_UMLClassInstance newInst = (G_UMLClassInstance)selected;
                G_UMLInstanceDialog dialog = new G_UMLInstanceDialog(newInst.getUmlInstance(), this, false);
                try{
                    dialog.showDialog(parent.getStage());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                newInst.updateText();
            }
            else if(Objects.equals(selected.getType(), "UMLMessage")){
                //todo
            }

            else if(Objects.equals(selected.getType(), "UMLReturnMessage")){
                //todo
            }


        });

        /* Delete button */
        //Clicking the Delete button
        sequenceDiagramScene.getRoot().lookup("#Delete").setOnMouseClicked(e ->{
            if(e.isStillSincePress()){
                if(Objects.equals(selected.getType(), "UMLClassInstance")){
                    deleteUMLInstance((G_UMLClassInstance)selected);
                }
                //todo else


            }
        });

        //TODO: newMessage button


    }

    /**
     * Method which deletes GuiUMLInstace with deep consequence (removes according messages, removes from uml)
     * @param guiToDelete guiInstance to be deleted
     */
    private void deleteUMLInstance(G_UMLClassInstance guiToDelete){
        UMLClassInstance umlToDelete = guiToDelete.getUmlInstance();
        diagram.removeInstance(umlToDelete); //remove instance from UML
        instances.remove(guiToDelete);
        ((Group)getNode()).getChildren().remove(guiToDelete.getNode());
        /*Delete all messages, which have go from or to the deleted Instance */
        deleteBelongingMessages(umlToDelete.getId()); //delete
    }

    /**
     * Method which deletes all messages which have argument Instance in 'from' or 'to'
     * @param instanceName
     */
    private void deleteBelongingMessages(String instanceName) {

        for(Iterator<G_UMLMessage> it = messages.iterator(); it.hasNext();){
            G_UMLMessage guiMessage = it.next();
            /* If equal, then remove message*/
            if(Objects.equals(guiMessage.getFrom(), instanceName) || Objects.equals(guiMessage.getTo(), instanceName)){
                it.remove(); //remove from GUI
                UMLMessage umlToDelete = guiMessage.getUMLMessage();
                diagram.removeMessage(umlToDelete); //remove from UML
                ((Group)getNode()).getChildren().remove(guiMessage.getNode()); //remove from Scene
            }
        }
    }


    /**
     * Method which deletes the selected message
     * @param guiToDelete GUI message to be deleted
     */
    private void deleteUMLMessage(G_UMLMessage guiToDelete){
        UMLMessage umlToDelete = guiToDelete.getUMLMessage();
        diagram.removeMessage(umlToDelete);
        messages.remove(guiToDelete);
        ((Group)getNode()).getChildren().remove(guiToDelete.getNode());
    }

    /**
     * Method which adds new GUIUMLInstance from existing umlInstance
     * @param newInstance umlInstance to be used for GuiUMLInstance
     * @throws IOException
     */
    private void addUMLInstance(UMLClassInstance newInstance) throws IOException {
        G_UMLClassInstance newGUMLInstance = new G_UMLClassInstance(newInstance, this);
        diagram.addInstance(newInstance);
        instances.add(newGUMLInstance);
        root.getChildren().add(newGUMLInstance.getNode());
    }



    /**
     * Method to update messages and their position
     * @throws IOException
     */
    public void updateMessages() throws IOException{
        for (G_UMLMessage m: messages)
            m.updatePosition();
    }


    public void removeInstance(G_UMLClassInstance instanceToRemove){
        instances.remove(instanceToRemove);
    }

    public Node getNode(){return root;}


    /**
     * Method which searches through messages and gives them current InstanceNames
     * @param oldName old name of instance
     * @param newName new name of instance
     */
    public void updatedInstanceName(String oldName, String newName){
        for (UMLMessage mes: diagram.getMessages()){
            /*Update from */
            if(Objects.equals(mes.getFrom(), oldName)) {
                mes.setFrom(newName);
            }
            /*Update to */
            if(Objects.equals(mes.getTo(), oldName)) {
                mes.setTo(newName);
            }
        }
    }

    /**
     * Method which sets the selected element //todo
     * @param selected Object which was selected (Instance or Message)
     */
    public void setSelected(G_selectable selected) {
        if(this.selected != null)
            this.selected.setSelect(false);
        this.selected = selected;
        sequenceDiagramScene.getRoot().lookup("#Edit").setDisable(false);
        sequenceDiagramScene.getRoot().lookup("#Delete").setDisable(false);
    }

    /**
     * Method which gets the order of message
     * @param m message to be searched for
     * @return
     */
    public Integer getMessageOrder(G_UMLMessage m){
        return messageOrder.indexOf(m);
    }

    /**
     * Method which gets UMLSequenceDiagram
     * @return UMLSequenceDiagram
     */
    public UMLSequenceDiagram getDiagram(){
        return diagram;
    }

    /**
     * todo
     * @return
     */
    public String getName(){
        return diagram.getName();
    }

    /**
     * Method to get G_UMLInstance by name
     * @param name Name of instance to be searched for
     * @return Found instance, otherwise null
     */
    public G_UMLClassInstance getGUMLClassInstance(String name){
        for(G_UMLClassInstance gInst : instances){
            if(Objects.equals(gInst.getName(), name)){
                return gInst;
            }
        }
        return null;
    }

    /**
     * Gets the sequence diagram scene
     * @return scene
     */
    public Scene getScene(){
        //todo zavolat metodu na konzistenci
        return sequenceDiagramScene;
    }

    //todo vola se vzdy, kdy se vola getscene
    //kontrola, zda vsechno sedi s classdiagramem, ostatni obarvit cervene
}

