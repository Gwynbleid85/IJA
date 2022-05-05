package ija.app.gui;

import ija.app.uml.sequenceDiagram.UMLMessage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class G_UMLMessage {
    private UMLMessage message;
    private G_UMLSequenceDiagram parent ;
    private Line root;

    public G_UMLMessage(UMLMessage mes, G_UMLSequenceDiagram sd) throws IOException {
        message = mes;
        parent = sd;
        root = FXMLLoader.load(getClass().getResource("fxml/G_UMLMessage.fxml"));

        //((Label)root.lookup("#labelMessage")).setText(message.getMessage() + "()");//todo
        setEventHandlers();
        updatePosition();
    }


    private void setEventHandlers(){
        /*Dragging UMLMessage */
        root.setOnMouseDragged(e -> {
           root.setStartY(e.getSceneY());
           root.setEndY(e.getSceneY());
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

        root.setStartX(from.x);
        root.setEndX(to.x);
        root.setStartY(from.y);
        root.setEndY(to.y);
    }


    public Node getNode() {
        return root;
    }
}
