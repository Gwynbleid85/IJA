package ija.app.gui;


import ija.app.gui.dialogs.*;
import ija.app.history.History;
import ija.app.uml.UML;
import ija.app.uml.classDiagram.UMLClass;
import ija.app.uml.classDiagram.UMLClassDiagram;
import ija.app.uml.classDiagram.UMLClassMethod;
import ija.app.uml.sequenceDiagram.UMLClassInstance;
import ija.app.uml.sequenceDiagram.UMLMessage;
import ija.app.uml.sequenceDiagram.UMLSequenceDiagram;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.File;
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
        messages = new ArrayList<>();

        /* Create new Scene */
        AnchorPane template = FXMLLoader.load(getClass().getResource("fxml/G_SequenceDiagramScene.fxml"));
        template.getChildren().add(root);
        sequenceDiagramScene = new Scene(template);
        sequenceDiagramScene.getRoot().lookup("#Edit").setDisable(true);
        sequenceDiagramScene.getRoot().lookup("#Delete").setDisable(true);

        /* Create gui instances for the according diagram */
        instances = new ArrayList<>();
        for (UMLClassInstance inst : sd.getInstances()) {
            addUMLInstance(inst, true);
        }
        setEventHandlers();
        positionLifelines();
        /*Resize the lifeLine when resizing the window size */
        parent.getStage().heightProperty().addListener((obs, oldVal, newVal) -> {
            positionLifelines();
        });
    }

    /**
     * Method which adds new GUIUMLInstance from existing umlInstance
     * @param newInstance umlInstance to be used for GuiUMLInstance
     * @param addingFromFile if already exists in UML and therefore shouldt be added into UML List
     * @throws IOException
     */
    private void addUMLInstance(UMLClassInstance newInstance, boolean addingFromFile) throws IOException {
        G_UMLClassInstance newGUMLInstance = new G_UMLClassInstance(newInstance, this);
        instances.add(newGUMLInstance);

        double offsetY;
        if(!addingFromFile){
            diagram.addInstance(newInstance);
            offsetY = 0;
        }
        else {
            offsetY = ((70 + 200 * (instances.size() - 1)));
        }
        Node x = newGUMLInstance.getNode();
        x.setLayoutX(offsetY);
        x.setLayoutY(50);
        root.getChildren().add(x);
    }


    /**
     * Method which adds new GUIUMLInstance from existing umlInstance
     * @param newMessage umlInstance to be used for GuiUMLInstance
     * @throws IOException
     */
    private void addUMLMessage(UMLMessage newMessage, boolean isReturn) throws IOException {
        G_UMLMessage newGUMLMessage = new G_UMLMessage(newMessage, this);
        messages.add(newGUMLMessage);
        diagram.addMessage(newMessage);
        newGUMLMessage.setPositionY((double) (300));
        root.getChildren().add(newGUMLMessage.getNode());
        root.getChildren().add(newGUMLMessage.getArrow());
    }

    /**
     * Method which draws messages, needs to be called after showing instances
     * @throws IOException
     */
    public void draw() throws IOException{
        System.out.println("Drawing");
        //create gui Messages for the according diagram
        for (UMLMessage m: diagram.getMessages()){
            G_UMLMessage gm = new G_UMLMessage(m, this);
            messages.add(gm);
            messageOrder.add(gm); //for use of getting position
            gm.setPositionY((double) (110 + getMessageOrder(gm) * 50));
            //add arrow
            root.getChildren().add(gm.getArrow());
            root.getChildren().add(gm.getNode());
        }
        setMenu();
        updateMessages();
        positionLifelines();
    }

    /**
     * Method which positions lifelines to center of Instance
     */
    public void positionLifelines(){
        for (G_UMLClassInstance gInst: instances){
            Line lifeLine = ((Line)gInst.getNode().lookup("#lifeLine"));
            lifeLine.setStartX(((AnchorPane)gInst.getNode()).getWidth()/2);
            lifeLine.setEndX(((AnchorPane)gInst.getNode()).getWidth()/2);
            lifeLine.setEndY(getNode().getScene().getHeight() - 50);
        }
    }



    public void setMenu(){
        ((MenuBar)sequenceDiagramScene.lookup("#MenuBar")).getMenus().clear();
        /* Set file op buttons*/
        MenuItem load = new MenuItem("Load from file");
        MenuItem save = new MenuItem("Save to file");
        load.setOnAction( e -> {
            try {
                G_LoadFile dialog = new G_LoadFile(parent.getStage(), parent);
                UML newUML = dialog.showDialog();
                parent.changeUML(newUML);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        });
        save.setOnAction( e -> {
            /* Save classes positions */
            parent.getClassDiagram().saveFilePositions();
            /* Save everything to file */
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showSaveDialog(parent.getStage());
            parent.getUml().storeDiagramsToFile(file.getAbsolutePath());
        });
        Menu fileMenu = new Menu("File");
        fileMenu.getItems().add(load);
        fileMenu.getItems().add(save);

        /* Set history buttons */
        MenuItem undo = new MenuItem("Undo (Ctrl+z)");
        MenuItem redo = new MenuItem("Redo (Ctrl+Shift+z)");
        undo.setOnAction( e -> {
            History.undoStatic();
        });
        redo.setOnAction( e -> {
            History.redoStatic();
        });
        Menu edit = new Menu("Edit");
        edit.getItems().add(undo);
        edit.getItems().add(redo);
        /* Add all to menuBar*/
        ((MenuBar)sequenceDiagramScene.lookup("#MenuBar")).getMenus().add(fileMenu);
        ((MenuBar)sequenceDiagramScene.lookup("#MenuBar")).getMenus().add(edit);

        /* Changing diagrams buttons */
        /* Class diagram button */
        sequenceDiagramScene.lookup("#SetClassDiagram").setOnMouseClicked( e -> {
            System.out.println("Changing to class diagram 1");
            parent.setScene(0, "");
        });

        /* Sequence diagram button */
        List<String> asdf = new ArrayList<>();
        for(UMLSequenceDiagram s : parent.getSequenceDiagrams())
        		asdf.add(s.getName());
        ObservableList<String> seqDiagrams = FXCollections.observableArrayList(asdf);
        ((ComboBox<String>)sequenceDiagramScene.lookup("#SetSeqDiagram")).getItems().clear();
        ((ComboBox<String>)sequenceDiagramScene.lookup("#SetSeqDiagram")).setItems(seqDiagrams);

        ((ComboBox<String>)sequenceDiagramScene.lookup("#SetSeqDiagram")).promptTextProperty().setValue(this.diagram.getName());

        ((ComboBox<String>)sequenceDiagramScene.lookup("#SetSeqDiagram")).setOnAction( e -> {
            parent.setScene(1, ((ComboBox<String>) sequenceDiagramScene.lookup("#SetSeqDiagram")).getSelectionModel().getSelectedItem());
        });

        ((Button)sequenceDiagramScene.lookup("#newSeqDia")).setOnAction(e -> {
            // New Sequence diagram
            try {
                G_NewSeqDiagram dialog = new G_NewSeqDiagram(parent.getStage(), parent);
                String newName = dialog.showDialog();
                parent.setScene(1, newName);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

    }

    /**
     * Set event handlers
     */
    private void setEventHandlers(){
        /*If the canvas is selected (no element is selected) */
        sequenceDiagramScene.getRoot().setOnMouseClicked(e -> {
            if(e.isStillSincePress()){
                if(selected != null)
                    selected.setSelect(false);
                selected = null;
                sequenceDiagramScene.getRoot().lookup("#Edit").setDisable(true);
                sequenceDiagramScene.getRoot().lookup("#Delete").setDisable(true);
            }
        });


        /* New Instance button*/
        sequenceDiagramScene.getRoot().lookup("#newInstance").setOnMouseClicked(e->{
            //create empty new Instance
            System.out.println("Create new instance!");

            UMLClassInstance newInstance = new UMLClassInstance(parent.getUml().getClassDiagram());
            G_UMLInstanceDialog dialog = new G_UMLInstanceDialog(newInstance, this, true);

            /*Show the created dialog*/
            boolean isChanged;
            try{
                isChanged = dialog.showDialog(parent.getStage());
            }catch(IOException ex){
                throw new RuntimeException(ex);
            }
            //todo only if save button clicked!!!
            /*Add the prepared Instance*/
            if (isChanged){
                try{
                    addUMLInstance(newInstance, false);
                }
                catch (IOException ex){
                    throw new RuntimeException(ex);
                }
                positionLifelines();
            }
        });

        /*New message button */
        sequenceDiagramScene.getRoot().lookup("#newMessage").setOnMouseClicked(e->{
            if(instances.size() != 0){
                UMLMessage newMessage = new UMLMessage(parent.getUml().getClassDiagram());
                newMessage.setIsReturn(false);
                G_addMessageDialog dialog = new G_addMessageDialog(parent.getUml().getClassDiagram(), newMessage, this, false);
                /*Show the created dialog*/
                boolean isChanged;
                try{
                    isChanged = dialog.showDialog(parent.getStage());
                }catch(IOException ex){
                    throw new RuntimeException(ex);
                }
                /*Add the prepared Instance*/
                if(isChanged){
                    try{
                        addUMLMessage(newMessage, false);
                    }
                    catch (IOException ex){
                        throw new RuntimeException(ex);
                    }
                }
                try {
                    updateMessages();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

        });


        /*New return message button */
        sequenceDiagramScene.getRoot().lookup("#newReturnMessage").setOnMouseClicked(e->{
            if(instances.size() != 0){
                UMLMessage newMessage = new UMLMessage(parent.getUml().getClassDiagram());
                newMessage.setIsReturn(true);
                G_addMessageDialog dialog = new G_addMessageDialog(parent.getUml().getClassDiagram(), newMessage, this, true);
                /*Show the created dialog*/
                boolean isChanged;
                try{
                    isChanged = dialog.showDialog(parent.getStage());
                }catch(IOException ex){
                    throw new RuntimeException(ex);
                }
                /*Add the prepared Instance*/
                if(isChanged){
                    try{
                        addUMLMessage(newMessage, true);
                    }
                    catch (IOException ex){
                        throw new RuntimeException(ex);
                    }
                }

                try {
                    updateMessages();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        /*Edit button */
        sequenceDiagramScene.getRoot().lookup("#Edit").setOnMouseClicked(e->{
            /*Edit instance*/
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
            /*Edit message */
            else if(Objects.equals(selected.getType(), "UMLMessage")){
                /*If editing a return message */
                if(((G_UMLMessage)selected).isReturnMessage()){
                    G_UMLMessage mes = (G_UMLMessage)selected;
                    G_editMessageDialog dialog = new G_editMessageDialog(parent.getUml().getClassDiagram(), mes.getUMLMessage(), this, true);
                    try{
                        dialog.showDialog(parent.getStage());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    mes.updateText();
                }
                /*If editing a method message */
                else {
                    G_UMLMessage mes = (G_UMLMessage)selected;
                    G_editMessageDialog dialog = new G_editMessageDialog(parent.getUml().getClassDiagram(), mes.getUMLMessage(), this, false);
                    try{
                        dialog.showDialog(parent.getStage());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    mes.updateText();
                }
            }
        });

        /* Delete button */
        //Clicking the Delete button
        sequenceDiagramScene.getRoot().lookup("#Delete").setOnMouseClicked(e ->{
            if(e.isStillSincePress()){
                if(Objects.equals(selected.getType(), "UMLClassInstance")){
                    deleteUMLInstance((G_UMLClassInstance)selected);
                }
                if(Objects.equals(selected.getType(), "UMLMessage")){
                    deleteUMLMessage((G_UMLMessage) selected);
                }
            }
        });


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
                ((Group)getNode()).getChildren().remove(guiMessage.getArrow());//remove arrow from Scene
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
        ((Group)getNode()).getChildren().remove(guiToDelete.getArrow());//remove arrow from Scene
        ((Group)getNode()).getChildren().remove(guiToDelete.getNode());
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
        checkConsistency();
        return sequenceDiagramScene;
    }

    public void checkConsistency(){
        UMLClassDiagram classDiagram = parent.getUml().getClassDiagram();
        Set<UMLClass> classes = classDiagram.getClasses();
        //check Instances
        checkInstancesConsistency(classes);
        //check messages 'from' and 'to'
        //checkMessageToFromConsistency(classes); todo
        //check messages text
        checkMessageConsistency(classes, classDiagram);
    }
    public void checkInstancesConsistency(Set<UMLClass> classes){
        boolean consistent;
        //check instances (existing class)
        for (G_UMLClassInstance guiInstance: instances){
            consistent = false;
            for (UMLClass classToCompare: classes){
                if(Objects.equals(classToCompare.getName(), guiInstance.getUmlInstance().getClassName())){
                    guiInstance.setIsConsistent(true);
                    consistent = true;
                    break;
                }
            }
            if(!consistent){
                guiInstance.setIsConsistent(false);
                //style elements
                guiInstance.getNode().lookup("#instanceLabel").setStyle( "-fx-text-fill: red; -fx-background-color : white; -fx-border-color : red; -fx-border-width : 1 ;");
                guiInstance.getNode().lookup("#lifeLine").setStyle("-fx-stroke: red; -fx-stroke-width: 1; -fx-stroke-dash-array: 10");
            }
        }
    }

    public void checkMessageToFromConsistency(Set<UMLClass> classes){
        boolean fromConsistent, toConsistent;
        //check messages(existing classes)
        for (G_UMLMessage guiMessage: messages){
            fromConsistent = false;
            toConsistent = false;
            for (UMLClass classToCompare: classes){
                if(Objects.equals(classToCompare.getName(), guiMessage.getUMLMessage().getClassFrom())){
                    fromConsistent = true;
                }
                if(Objects.equals(classToCompare.getName(), guiMessage.getUMLMessage().getClassTo())){
                    toConsistent = true;
                }
            }
            if(fromConsistent && toConsistent){ //if from or to Class doesnt match
                guiMessage.setIsConsistent(true);
            }
            else {
                guiMessage.setIsConsistent(false);
                //style elements
                if (guiMessage.getIsLeftarrow()){
                    guiMessage.getArrow().lookup("#leftArrowUp").setStyle("-fx-stroke: red; -fx-stroke-width : 1;");
                    guiMessage.getArrow().lookup("#leftArrowDown").setStyle("-fx-stroke: red; -fx-stroke-width : 1;");
                }else{
                    guiMessage.getArrow().lookup("#rightArrowUp").setStyle("-fx-stroke: red; -fx-stroke-width : 1;");
                    guiMessage.getArrow().lookup("#rightArrowDown").setStyle("-fx-stroke: red; -fx-stroke-width : 1;");
                }
                guiMessage.getNode().lookup("#messageLabel").setStyle("-fx-text-fill: red; -fx-font-weight: regular;");

                if(guiMessage.isReturnMessage())
                    guiMessage.getNode().lookup("#messageLine").setStyle("-fx-stroke: red; -fx-stroke-width : 1; -fx-stroke-dash-array: 10;");
                else
                    guiMessage.getNode().lookup("#messageLine").setStyle("-fx-stroke: red; -fx-stroke-width : 1;");
            }
        }
    }

    public void checkMessageConsistency(Set<UMLClass> classes, UMLClassDiagram classDiagram){
        boolean isConsistent;
        for (G_UMLMessage guiMessage: messages){
            isConsistent = false;
            //check only if is not a return message
            if(!guiMessage.isReturnMessage()){
                List<UMLClassMethod> ownMethods= classDiagram.getUMLClassOwnMethods(guiMessage.getUMLMessage().getClassTo());
                List<UMLClassMethod> inheritedMethods = classDiagram.getUMLClassInheritedMethods(guiMessage.getUMLMessage().getClassTo());
                if(inheritedMethods == null){
                    inheritedMethods = new ArrayList<>();
                }
                List<UMLClassMethod> allMethods = new ArrayList<>();
                allMethods.addAll(ownMethods);
                allMethods.addAll(inheritedMethods);
                //check if the message corresponds to a method in 'To' class
                for(UMLClassMethod method: allMethods){
                    if(Objects.equals(guiMessage.getUMLMessage().getMessage(), method.getName())){
                        guiMessage.setIsConsistent(true);
                        isConsistent = true;
                        break;
                    }
                }
                //if is not consistent, then set styling
                if(!isConsistent){
                    guiMessage.setIsConsistent(false);
                    //style elements
                    if (guiMessage.getIsLeftarrow()){
                        guiMessage.getArrow().lookup("#leftArrowUp").setStyle("-fx-stroke: red; -fx-stroke-width : 1;");
                        guiMessage.getArrow().lookup("#leftArrowDown").setStyle("-fx-stroke: red; -fx-stroke-width : 1;");
                    }else{
                        guiMessage.getArrow().lookup("#rightArrowUp").setStyle("-fx-stroke: red; -fx-stroke-width : 1;");
                        guiMessage.getArrow().lookup("#rightArrowDown").setStyle("-fx-stroke: red; -fx-stroke-width : 1;");
                    }
                    guiMessage.getNode().lookup("#messageLabel").setStyle("-fx-text-fill: red; -fx-font-weight: regular;");

                    if(guiMessage.isReturnMessage())
                        guiMessage.getNode().lookup("#messageLine").setStyle("-fx-stroke: red; -fx-stroke-width : 1; -fx-stroke-dash-array: 10;");
                    else
                        guiMessage.getNode().lookup("#messageLine").setStyle("-fx-stroke: red; -fx-stroke-width : 1;");
                }
            }
            //if is a return message
            else{
                guiMessage.setIsConsistent(true);
            }
        }
    }
}

