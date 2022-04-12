package ija.app.uml.sequenceDiagram;

import ija.app.uml.classDiagram.ClassDiagram;
import ija.app.uml.classDiagram.UMLClass;

import java.util.Objects;


public class UMLClassInstance {
    private String instanceName; //name will be concatenated from instance name, ':' and class name
    private String uMLClassName;
    private String id;

    public UMLClassInstance(ClassDiagram classDiagram, String instanceName, String uMLClassName) {
        if (classDiagram.getClasses().contains(uMLClassName)) { //TODO
            this.instanceName = instanceName;
            this.uMLClassName = uMLClassName;
            this.id = instanceName + ":" + uMLClassName;
        }
        //pokud ne, tak se jen nevytvori
    }

    public String getId(){
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getuMLClassName(){
        return this.uMLClassName;
    }

    public void setuMLClassName(String uMLClassName){
        this.uMLClassName = uMLClassName;
    }

    public String getInstanceName(){
        return this.instanceName;
    }

    public void setInstanceName(String instanceName){
        this.instanceName = instanceName;
    }
















    @Override
    public boolean equals(Object o) {

        if(o instanceof UMLClassInstance){
            UMLClassInstance toCompare = (UMLClassInstance) o;
            return this.id.equals(toCompare.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}
