package ija.app.gui;


import ija.app.gui.dialogs.G_AddUMLInstanceDialog;
import ija.app.uml.sequenceDiagram.UMLClassInstance;
import ija.app.uml.sequenceDiagram.UMLMessage;
import ija.app.uml.sequenceDiagram.UMLSequenceDiagram;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

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
        Pane template = FXMLLoader.load(getClass().getResource("fxml/G_SequenceDiagramScene.fxml"));
        template.getChildren().add(root);
        sequenceDiagramScene = new Scene(template);


        /* Create gui instances for the according diagram */
        instances = new ArrayList<>();
        for (UMLClassInstance inst : sd.getInstances()) {
            G_UMLClassInstance gInst = new G_UMLClassInstance(inst, this); //todo
            instances.add(gInst);
            Node x = gInst.getNode();
            x.setLayoutX(x.getLayoutX() + 200* (instances.size() - 1));
            root.getChildren().add(x);
        }


        //create gui Messages for the according diagram
        messages = new ArrayList<>();
        for (UMLMessage m: sd.getMessages()){
            G_UMLMessage gm = new G_UMLMessage(m, this); //todo
            messages.add(gm);
            messageOrder.add(gm); //for use of getting position
            root.getChildren().add(gm.getNode());
        }
        setEventHandlers();
        updateMessages(); //todo doesnt work at x axis at beginning
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
            }
        });


        /* New Instance button*/
        sequenceDiagramScene.getRoot().lookup("#newObject").setOnMouseClicked(e->{
            //create empty new Instance
            UMLClassInstance newInstance = new UMLClassInstance(parent.getUml().getClassDiagram());
            G_AddUMLInstanceDialog dialog = new G_AddUMLInstanceDialog(newInstance, this);

            /*Show the created dialog*/
            try{
                dialog.showDialog(parent.getStage());
            }catch(IOException ex){
                throw new RuntimeException(ex);
            }
            /*Add the prepared Instance*/
            try{
                addUMLInstance(newInstance);
            }
            catch (IOException ex){
                throw new RuntimeException(ex);
            }
        });

        /*Edit button */


        //TODO: newMessage button
        //TODO: newObject button
        //TODO: edit button


    }

    private void addUMLInstance(UMLClassInstance newInstance) throws IOException {
        G_UMLClassInstance newGUMLInstance = new G_UMLClassInstance(newInstance, this);
        diagram.addInstance(newInstance);
        instances.add(newGUMLInstance);
        root.getChildren().add(newGUMLInstance.getNode());

    }

    /**
     * Gets the sequence diagram scene
     * @return scene
     */
    public Scene getScene(){
        return sequenceDiagramScene;
    }


    /**
     * Method to get GUMLClassInstance by name
     */
    public G_UMLClassInstance getGUMLClassInstance(String name){
        for (G_UMLClassInstance gInst: instances){
            if(Objects.equals(gInst.getName(), name)){
                return gInst;
            }
        }
        return null;
    }


    /**
     * Method to update messages and their position
     * @throws IOException
     */
    public void updateMessages() throws IOException{
        for (G_UMLMessage m: messages)
            m.updatePosition();
    }


    public Node getNode(){return root;}

    /**
     * Method which sets the selected element //todo
     * @param selected Object which was selected (Instance or Message)
     */
    public void setSelected(G_selectable selected) {
        if(this.selected != null)
            this.selected.setSelect(false);
        this.selected = selected;
        sequenceDiagramScene.getRoot().lookup("#Edit").setDisable(false);
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
}

