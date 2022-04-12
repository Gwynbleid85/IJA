package ija.app.uml.sequenceDiagram;

import ija.app.uml.classDiagram.ClassDiagram;

public class UMLMessage {
    private String from;
    private String to;
    private String message;

    public UMLMessage(ClassDiagram classDiagram, String from, String to, String message){

        if (classDiagram.getClasses().contains(from)) { //TODO
            this.from = from;
        }

        if (classDiagram.getClasses().contains(to)) { //TODO
            this.to = to;
        }

        for (int i = 0; i < ClassDiagram.getClasses().size(); i++){
            if (ClassDiagram.getClasses().get(i).equals(from)) {
                if(ClassDiagram.getClasses().get(i).getMethods().contains(message))
                    this.message = message;
            }
        }
    }

    public String getFrom(){
        return this.from;
    }

    public void setFrom(String from){
        this.from = from;
    }

    public String getTo(){
        return this.to;
    }

    public void setTo(String to){
        this.to = to;
    }

    public String getMessage(){
        return this.message;
    }

    public void setMessage(String message){
        this.message = message;
    }



}
