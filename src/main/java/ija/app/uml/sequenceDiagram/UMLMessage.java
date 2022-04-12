package ija.app.uml.sequenceDiagram;

import ija.app.uml.classDiagram.UMLClassDiagram;

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

    public String getFrom(){
        return from;
    }

    public void setFrom(String from){
        this.from = from;
    }

    public String getTo(){
        return to;
    }

    public void setTo(String to){
        this.to = to;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public UMLClassDiagram getClassDiagram(){
        return classDiagram;
    }
}
