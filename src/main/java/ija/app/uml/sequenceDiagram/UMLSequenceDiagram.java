package ija.app.uml.sequenceDiagram;

import ija.app.uml.classDiagram.UMLClass;
import ija.app.uml.classDiagram.UMLClassDiagram;
import java.util.*;

/**
 * @author Jiri Mladek (xmlade01)
 * @date 12.4.2022
 * Class representing SequenceDiagram
 */
public class UMLSequenceDiagram {

    private String name;
    private List<UMLClassInstance> instances;
    private List<UMLMessage> messages;
    private UMLClassDiagram classDiagram;

    /**
     * Constructor of UMLSequenceDiagram
     * @param name Name of created UMLSequenceDiagram
     * @param classDiagram Class diagram, that sequence diagram refers to
     */
    public UMLSequenceDiagram(String name, UMLClassDiagram classDiagram){
        this.name = name;
        this.instances = new LinkedList<UMLClassInstance>();
        this.messages = new LinkedList<UMLMessage>();
        this.classDiagram = classDiagram;
    }

    /**
     * Method which gets name of Sequence diagram
     * @return name of sequence diagram
     */
    public String getName(){
        return name;
    }

    /**
     * Method which sets the name of Sequence diagram
     * @param name name to be assigned
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Method which gets list of instances from Sequence diagram
     * @return list of instances from Sequence diagram
     */
    public List<UMLClassInstance> getInstances(){
        return Collections.unmodifiableList(instances);
    }

    /**
     * Method which gets list of messages from Sequence diagram
     * @return list of messages from Sequence diagram
     */
    public List<UMLMessage> getMessages(){
        return Collections.unmodifiableList(messages);
    }

    /**
     * Method which adds instance to a List
     * @param newInstance Instance to be added to List
     * @return false if already exists, true if instance is added
     */
    public boolean addInstance(UMLClassInstance newInstance){
        if(instances.contains(newInstance))
            return false;
        else {
            instances.add(newInstance);
            return true;
        }
    }

    /**
     * Method which removes instance from List
     * @param newInstance Instance to be removed from List
     * @return true if instance to be removed exists
     */
    public boolean removeInstance(UMLClassInstance newInstance){
        return instances.remove(newInstance);
    }


    /**
     * Method which adds message to a List
     * @param newMessage Message to be added to List
     */
    public void addMessage(UMLMessage newMessage){
        messages.add(newMessage);
    }

    /**
     * Method which removes message from List
     * @param newMessage message to be removed from List
     * @return true if message to be removed exists
     */
    public boolean removeMessage(UMLMessage newMessage){
        return messages.remove(newMessage);
    }

    //TODO: change position






    @Override
    public boolean equals(Object o) {
        if(o instanceof UMLSequenceDiagram){
            UMLSequenceDiagram toCompare = (UMLSequenceDiagram) o;
            return this.name.equals(toCompare.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

}
