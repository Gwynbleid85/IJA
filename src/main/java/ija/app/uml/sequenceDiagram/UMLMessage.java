package ija.app.uml.sequenceDiagram;

import ija.app.uml.classDiagram.UMLClassDiagram;

/**
 * @author Jiri Mladek (xmlade01)
 * @date 12.4.2022
 * Class representing message between 2 instances
 */
public class UMLMessage {
    private String from;
    private String to;
    private String message;
    private UMLClassDiagram classDiagram;
    private UMLSequenceDiagram sequenceDiagram;

    /**
     * Constructor of UMLMesssage
     * @param classDiagram Class diagram, that UMLMessage refers to
     * @param from Name of instance, from which message goes
     * @param to Name of instance, to which message goes
     * @param message Content of message
     */
    public UMLMessage(UMLClassDiagram classDiagram, UMLSequenceDiagram sequenceDiagram, String from, String to, String message){
        this.classDiagram = classDiagram;
        this.sequenceDiagram = sequenceDiagram;
        this.from = from;
        this.to = to;
        this.message = message;
    }

    /**
     * Method to get name of 'from' instance
     * @return instance name, from which message goes
     */
    public String getFrom(){
        return from;
    }

    /**
     * Method which sets the name of 'from' instance
     * @param from name of instance, from which message goes
     */
    public void setFrom(String from){
        this.from = from;
    }

    /**
     * Method to get name of 'to' instance
     * @return instance name, to which message goes
     */
    public String getTo(){
        return to;
    }

    /**
     * Method which sets the name of 'to' instance
     * @param to name of instance, to which message goes
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
     * Method which checks consistency of UMLMessage
     * @return true if is consistent, false if not
     * @note implements consistency interface
     */
    public boolean consistencyCheck(){
        String[] arrFrom = from.split(":", 2);
        String[] arrTo = to.split(":", 2);
        String classFrom = arrFrom[1];
        String classTo = arrTo[1];

        //check if 'from' instance exists
        if(!sequenceDiagram.getInstances().contains(from)){
            return false;
        }
        //check if 'to' instance exists
        if(!sequenceDiagram.getInstances().contains(to)){
            return false;
        }
        //check if 'from' class exists
        if(!classDiagram.getClasses().contains(classFrom)){
            return false;
        }
        //check if 'to' class exists
        if(!classDiagram.getClasses().contains(classTo)){
            return false;
        }
        //check if there is an existing method for a message
        if(!(classDiagram.getUMLClassInheritedMethods(classTo).contains(message) || classDiagram.getUMLClassOwnMethods(classTo).contains(message))){
            return false;
        }
        return true;
    }

    /**
     * Method which gets the class diagram
     * @return classDiagram classDiagram
     */
    public UMLClassDiagram getClassDiagram(){
        return classDiagram;
    }

    /**
     * Method which gets the sequence diagram which message belongs to
     * @return sequenceDiagram sequence diagram
     */
    public UMLSequenceDiagram getSequenceDiagram(){
        return sequenceDiagram;
    }
}
