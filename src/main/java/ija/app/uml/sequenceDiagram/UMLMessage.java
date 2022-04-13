package ija.app.uml.sequenceDiagram;

import ija.app.uml.classDiagram.UMLClassDiagram;

/**
 * @author Jiri Mladek (xmlade01)
 * @date 12.4.2022
 * File for UMLMessage for Sequence diagram usage
 */

public class UMLMessage {
    private String from;
    private String to;
    private String message;
    private UMLClassDiagram classDiagram;

    /**
     * Constructor of UMLMesssage
     * @param classDiagram Class diagram, that UMLMessage refers to
     * @param from Name of class, from which message goes
     * @param to Name of class, to which message goes
     * @param message Content of message
     */
    public UMLMessage(UMLClassDiagram classDiagram, String from, String to, String message){
        this.from = from;
        this.to = to;
        this.message = message;
        this.classDiagram = classDiagram;
    }

    /**
     * Method to get name of'from' class
     * @return class name, from which message goes
     */
    public String getFrom(){
        return from;
    }

    /**
     * Method which sets the name of 'from' class
     * @param from name of class, from which message goes
     */
    public void setFrom(String from){
        this.from = from;
    }

    /**
     * Method to get name of 'to' class
     * @return class name, to which message goes
     */
    public String getTo(){
        return to;
    }

    /**
     * Method which sets the name of 'to' class
     * @param to name of class, to which message goes
     */
    public void setTo(String to){
        this.to = to;
    }

    /**
     * Method to get content of message between instances
     * @return message content
     */
    public String getMessage(){
        return message;
    }

    /**
     * Method to set content of message between instances
     */
    public void setMessage(String message){
        this.message = message;
    }

    /**
     * Method which gets the class diagram
     * @return classDiagram classDiagram
     */
    public UMLClassDiagram getClassDiagram(){
        return classDiagram;
    }
}
