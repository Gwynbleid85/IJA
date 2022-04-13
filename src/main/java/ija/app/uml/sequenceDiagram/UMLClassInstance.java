package ija.app.uml.sequenceDiagram;
import ija.app.uml.classDiagram.UMLClassDiagram;
import ija.app.uml.consistency;

/**
 * @author Jiri Mladek (xmlade01)
 * @date 12.4.2022
 * Class representing UMLClassInstance in Sequence diagram
 */
public class UMLClassInstance implements consistency{
    private String name;
    private String className;
    //final id will be concatenated from name, ':' and classname
    private UMLClassDiagram classDiagram;

    /**
     * Constructor of UMLClassInstance
     * @param classDiagram Class diagram, that UMLClassInstance refers to
     * @param name Name of instance
     * @param className Name of class to which instance refers to
     */
    public UMLClassInstance(UMLClassDiagram classDiagram, String name, String className) {
        this.name = name;
        this.className = className;
        this.classDiagram = classDiagram;
    }

    /**
     * Method for creating Instance ID
     * @return Instance ID
     */
    public String createId(){
        return name + ":" + className;
    }

    /**
     * Method which gets name of class
     * @return name of class
     */
    public String getClassName(){
        return className;
    }

    /**
     * Method which sets name of class
     * @param className className to be set
     */
    public void setClassName(String className){
        this.className = className;
    }

    /**
     * Method which gets name of instance
     * @return name of instance
     */
    public String getName(){
        return name;
    }

    /**
     * Method which sets name of instance
     * @param name name of instance to be set
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Method which gets the class diagram
     * @return classDiagram classDiagram
     */
    public UMLClassDiagram getClassDiagram(){
        return classDiagram;
    }

    /**
     * Method which checks, whether there is an existing class from ClassDiagram for ClassName
     * @return true if is consistent, false if not
     * @note implements consistency interface
     */
    public boolean consistencyCheck(){
        return classDiagram.getClasses().contains(className);
    }


    @Override
    public boolean equals(Object o) {
        if(o instanceof UMLClassInstance){
            UMLClassInstance toCompare = (UMLClassInstance) o;
            String id = name + ":" + className;
            String idCompare = toCompare.name + ":" + toCompare.className;
            return id.equals(idCompare);
        }
        return false;
    }

    @Override
    public int hashCode() {
        String id = name + ":" + className;
        return id.hashCode();
    }
}
