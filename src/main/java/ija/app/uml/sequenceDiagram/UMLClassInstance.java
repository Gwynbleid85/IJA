package ija.app.uml.sequenceDiagram;

import ija.app.uml.classDiagram.UMLClassDiagram;


public class UMLClassInstance {
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
    }

    public String createId(){
        return name + ":" + className;
    }

    public String getClassName(){
        return className;
    }

    public void setClassName(String className){
        this.className = className;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public UMLClassDiagram getClassDiagram(){
        return classDiagram;
    }

    //TODO:consistency

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
