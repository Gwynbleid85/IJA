package ija.app.uml.sequenceDiagram;

import ija.app.uml.classDiagram.UMLClass;

import java.util.*;

public class SequenceDiagram {

    private String name;
    private List<UMLClassInstance> instances;
    private List<UMLMessage> messages;


    public SequenceDiagram(String name){
        this.name = name;
        this.instances = new LinkedList<UMLClassInstance>();
        this.messages = new LinkedList<UMLMessage>();
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public LinkedList<UMLClassInstance> getInstances(){
        return Collections.unmodifiableList(this.instances);
    }

    public LinkedList<UMLMessage> getMessages(){
        return Collections.unmodifiableList(this.messages);
    }

    //pridani classinstance, kontrola zda uz neexistuje
    public boolean addClassInstance(UMLClassInstance newInstance){
        if(this.instances.contains(newInstance))
            return false;
        else {
            this.instances.add(newInstance);
            return true;
        }
    }

    public boolean removeClassInstance(UMLClassInstance newInstance){
        return this.instances.remove(newInstance);
    }

    public boolean addMessage(UMLMessage newMessage){
        if(this.messages.contains(newMessage))
            return false;
        else {
            this.messages.add(newMessage);
            return true;
        }
    }

    public boolean removeMessage(UMLMessage newMessage){
        return this.messages.remove(newMessage);
    }



    //Change position

}
