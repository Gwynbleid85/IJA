package ija.app.gui;

import ija.app.uml.sequenceDiagram.UMLMessage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class G_UMLMessage {
    private UMLMessage message;
    private G_UMLSequenceDiagram parent;
    private AnchorPane root;

    public G_UMLMessage(UMLMessage mes, G_UMLSequenceDiagram sd) throws IOException {
        message = mes;
        parent = sd;
        /*If is a message with method */
        if(!message.getIsReturn()){
            root = FXMLLoader.load(getClass().getResource("fxml/G_UMLMessage.fxml"));
            ((Label)root.lookup("#messageLabel")).setText(message.getMessage() + "()");
        }
        /*If is a return message */
        else{
            root = FXMLLoader.load(getClass().getResource("fxml/G_UMLReturnMessage.fxml"));
            ((Label)root.lookup("#messageLabel")).setText(message.getMessage());
        }
        //todo: message which from one instance to same instance
        setEventHandlers();
        updatePosition();
    }


    private void setEventHandlers(){
        /*Dragging UMLMessage */
        root.setOnMouseDragged(e -> {
            ((Line)root.lookup("#messageLine")).setStartY(e.getSceneY());
            ((Line)root.lookup("#messageLine")).setEndY(e.getSceneY());
            try {
                parent.updateMessages();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        });
    }


    public void updatePosition() throws IOException {
        G_Position from;
        G_Position to;
        try{
            from = parent.getGUMLClassInstance(message.getFrom()).getPos(this);
            to = parent.getGUMLClassInstance(message.getTo()).getPos(this);
        }
        catch (Exception e){
            from = new G_Position(0, 0);
            to = new G_Position(0, 0);
        }

        /*
        Line messageLine = (Line)root.getGraphic();
        if(from.x > to.x){
            messageLine.setStartX(to.x);
            messageLine.setStartY(to.y);
            messageLine.setEndX(from.x);
            messageLine.setEndY(from.y);
        }
        else {
            messageLine.setStartX(from.x);
            messageLine.setStartY(from.y);
            messageLine.setEndX(to.x);
            messageLine.setEndY(to.y);
        }
    */



        /*
        root.setLayoutX(from.x);
        root.setLayoutY(from.y);


        Double length = from.x - to.x;
        root.setPrefWidth(length);
        */
        Line messageLine = ((Line)root.lookup("#messageLine"));
        messageLine.setStartX(from.x);
        messageLine.setEndX(to.x);
        messageLine.setStartY(from.y);
        messageLine.setEndY(to.y);
    }


    public Node getNode() {
        return root;
    }

    /**
     * Method which gets the UML representation of message
     * @return UMLMessage
     */
    public UMLMessage getUMLMessage() {
        return message;
    }

    /**
     * Method which gets 'from' instance of message
     * @return from instance
     */
    public String getFrom(){
        return message.getFrom();
    }

    /**
     * Method which gets 'to' instance of message
     * @return to instance
     */
    public String getTo(){
        return message.getTo();
    }

    /**
     * Method which gets type of message
     * @return true if is a return message
     */
    public Boolean isReturnMessage(){
        return message.getIsReturn();
    }
}
