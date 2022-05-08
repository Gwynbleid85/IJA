package ija.app.gui;

import ija.app.uml.sequenceDiagram.UMLMessage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

import java.io.IOException;

public class G_UMLMessage implements G_selectable{
    private UMLMessage message;
    private G_UMLSequenceDiagram parent;
    private AnchorPane GumlMessage;
    private AnchorPane arrow;
    private boolean isLeftarrow;
    private double positionY;
    private boolean isConsistent;

    public G_UMLMessage(UMLMessage mes, G_UMLSequenceDiagram sd) throws IOException {
        message = mes;
        parent = sd;
        arrow = FXMLLoader.load(getClass().getResource("fxml/arrow.fxml"));

        /*If is a message with method */
        if(!message.getIsReturn()){
            GumlMessage = FXMLLoader.load(getClass().getResource("fxml/G_UMLMessage.fxml"));
            ((Label)GumlMessage.lookup("#messageLabel")).setText(message.getMessage() + "()");
        }
        /*If is a return message */
        else{
            GumlMessage = FXMLLoader.load(getClass().getResource("fxml/G_UMLReturnMessage.fxml"));
            ((Label)GumlMessage.lookup("#messageLabel")).setText(message.getMessage());
        }
        setEventHandlers();
        updatePosition();
    }


    private void setEventHandlers(){
        /*Dragging UMLMessage */
        GumlMessage.setOnMouseDragged(e -> {
            this.positionY = e.getSceneY();
            GumlMessage.setLayoutY(positionY);
            arrow.setLayoutY(positionY + 12);
        });

        /*Selecting UMLMessage */
        GumlMessage.setOnMouseClicked(e -> {
            if(e.isStillSincePress()){
                setSelect(true);
                e.consume(); // differenciate selecting Instance and canvas
            }
        });
    }


    /**
     * Method which sets the Message to be selected and sets style
     * @param selected boolean if should be or should not be selected
     */
    @Override
    public void setSelect(boolean selected) {
        if(selected){
            parent.setSelected(this);
            if (isLeftarrow){
                arrow.lookup("#leftArrowUp").setStyle("-fx-stroke-width : 2;");
                arrow.lookup("#leftArrowDown").setStyle("-fx-stroke-width : 2;");
            }else{
                arrow.lookup("#rightArrowUp").setStyle("-fx-stroke-width : 2;");
                arrow.lookup("#rightArrowDown").setStyle("-fx-stroke-width : 2;");
            }
            GumlMessage.lookup("#messageLabel").setStyle("-fx-font-weight: bold;");

            if(isReturnMessage())
                GumlMessage.lookup("#messageLine").setStyle("-fx-stroke-width : 2; -fx-stroke-dash-array: 10;");
            else
                GumlMessage.lookup("#messageLine").setStyle("-fx-stroke-width : 2;");
        }
        else{
            //if is consistent
            if(isConsistent){
                if (isLeftarrow){
                    arrow.lookup("#leftArrowUp").setStyle("-fx-stroke-width : 1;");
                    arrow.lookup("#leftArrowDown").setStyle("-fx-stroke-width : 1;");
                }else{
                    arrow.lookup("#rightArrowUp").setStyle("-fx-stroke-width : 1;");
                    arrow.lookup("#rightArrowDown").setStyle("-fx-stroke-width : 1;");
                }
                GumlMessage.lookup("#messageLabel").setStyle("-fx-font-weight: normal;");

                if(isReturnMessage())
                    GumlMessage.lookup("#messageLine").setStyle("-fx-stroke-width : 1; -fx-stroke-dash-array: 10;");
                else
                    GumlMessage.lookup("#messageLine").setStyle("-fx-stroke-width : 1;");
            }
            else{
                if (isLeftarrow){
                    arrow.lookup("#leftArrowUp").setStyle("-fx-stroke: red; -fx-stroke-width : 1;");
                    arrow.lookup("#leftArrowDown").setStyle("-fx-stroke: red; -fx-stroke-width : 1;");
                }else{
                    arrow.lookup("#rightArrowUp").setStyle("-fx-stroke: red; -fx-stroke-width : 1;");
                    arrow.lookup("#rightArrowDown").setStyle("-fx-stroke: red; -fx-stroke-width : 1;");
                }
                GumlMessage.lookup("#messageLabel").setStyle("-fx-text-fill: red; -fx-font-weight: normal;");

                if(isReturnMessage())
                    GumlMessage.lookup("#messageLine").setStyle("-fx-stroke: red; -fx-stroke-width : 1; -fx-stroke-dash-array: 10;");
                else
                    GumlMessage.lookup("#messageLine").setStyle("-fx-stroke: red; -fx-stroke-width : 1;");
            }

        }
    }


    /**
     * Method which gets the type of selected item
     * @return UMLMessage
     */
    @Override
    public String getType() {
        return "UMLMessage";
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

        Line messageLine = ((Line)GumlMessage.lookup("#messageLine"));

        if(from.x < to.x){
            isLeftarrow = false;
            GumlMessage.setLayoutX(from.x);
            messageLine.setStartX(from.x);
            messageLine.setEndX(to.x);
            GumlMessage.setLayoutY(from.y);

            //set position of arrow
            arrow.setLayoutX(to.x - 15);
            arrow.setLayoutY(to.y + 12);
            arrow.lookup("#leftArrowUp").setVisible(false);
            arrow.lookup("#leftArrowDown").setVisible(false);
            arrow.lookup("#rightArrowUp").setVisible(true);
            arrow.lookup("#rightArrowDown").setVisible(true);

        }
        else {
            isLeftarrow = true;
            GumlMessage.setLayoutX(to.x);
            messageLine.setStartX(to.x);
            messageLine.setEndX(from.x);
            GumlMessage.setLayoutY(from.y);

            //set position of arrow
            //set position of arrow
            arrow.setLayoutX(to.x);
            arrow.setLayoutY(to.y + 12);
            arrow.lookup("#leftArrowUp").setVisible(true);
            arrow.lookup("#leftArrowDown").setVisible(true);
            arrow.lookup("#rightArrowUp").setVisible(false);
            arrow.lookup("#rightArrowDown").setVisible(false);
        }

    }


    public Node getNode() {
        return GumlMessage;
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

    public Double getPositionY(){
        return positionY;
    }

    public void setPositionY(Double positionY){
        this.positionY = positionY;
    }

    public Node getArrow(){
        return (Node)arrow;
    }

    /*Method which updates text of message */
    public void updateText() {
        if(isReturnMessage())
            ((Label)GumlMessage.lookup("#messageLabel")).setText(message.getMessage());
        else
            ((Label)GumlMessage.lookup("#messageLabel")).setText(message.getMessage() + "()");

    }

    public boolean getIsConsistent() {
        return isConsistent;
    }

    public void setIsConsistent(boolean consistent) {
        isConsistent = consistent;
    }

    public boolean getIsLeftarrow(){
        return isLeftarrow;
    }
}
