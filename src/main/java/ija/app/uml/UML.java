package ija.app.uml;

import ija.app.uml.classDiagram.UMLClassDiagram;
import ija.app.uml.sequenceDiagram.UMLSequenceDiagram;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class UML {
    private UMLClassDiagram classDiagram;
    private List<UMLSequenceDiagram> sequenceDiagrams;

    public UML(UMLClassDiagram classDiagram, Set<UMLSequenceDiagram>sequenceDiagrams){
        this.classDiagram = classDiagram;
        this.sequenceDiagrams = new LinkedList<UMLSequenceDiagram>();
    }

    public UMLClassDiagram getClassDiagram(){
        return classDiagram;
    }

    public void setClassDiagram(UMLClassDiagram classDiagram){
        this.classDiagram = classDiagram;
    }

    public List<UMLSequenceDiagram> getSequenceDiagrams() {
        return sequenceDiagrams;
    }

    public boolean addSequenceDiagram(UMLSequenceDiagram newSequenceDiagram){
        if(sequenceDiagrams.contains(newSequenceDiagram))
            return false;
        else{
            sequenceDiagrams.add(newSequenceDiagram);
            return true;
        }
    }

    public boolean removeSequenceDiagram(UMLSequenceDiagram sequenceDiagram){
        return sequenceDiagrams.remove(sequenceDiagram);
    }

    public UML loadDiagrams(){
        return null;
    }

    public void storeDiagrams(UML uml){

    }

}
