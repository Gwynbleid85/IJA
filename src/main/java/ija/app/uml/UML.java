package ija.app.uml;

import ija.app.uml.classDiagram.UMLClass;
import ija.app.uml.classDiagram.UMLClassDiagram;
import ija.app.uml.sequenceDiagram.UMLSequenceDiagram;

import java.io.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Jiri Mladek (xmlade01)
 * @date 20.4.2022
 * Class which gathers all diagrams
 */
public class UML {
    private UMLClassDiagram classDiagram;
    private List<UMLSequenceDiagram> sequenceDiagrams;



    /**
     * Constructor of UML
     */
    public UML(){
        this.classDiagram = new UMLClassDiagram();
        this.sequenceDiagrams = new LinkedList<>();
    }

    /**
     * Method which gets the Class Diagram
     * @return classdiagram
     */
    public UMLClassDiagram getClassDiagram(){
        return classDiagram;
    }

    /**
     * Method which sets the classdiagram
     * @param classDiagram classDiagram to be set
     */
    public void setClassDiagram(UMLClassDiagram classDiagram){
        this.classDiagram = classDiagram;
    }

    /**
     * Method which gets list of all Sequence diagrams
     * @return list of sequence diagrams
     */
    public List<UMLSequenceDiagram> getSequenceDiagrams() {
        return sequenceDiagrams;
    }

    /**
     * Method which adds sequence diagram to the list
     * @param newSequenceDiagram sequence diagram to be added
     * @return true if is added, false if sequence diagram with same name already exists
     */
    public boolean addSequenceDiagram(UMLSequenceDiagram newSequenceDiagram){
        if(sequenceDiagrams.contains(newSequenceDiagram))
            return false;
        else{
            sequenceDiagrams.add(newSequenceDiagram);
            return true;
        }
    }

    /**
     * Method which removes sequence diagram from the list
     * @param sequenceDiagram sequence diagram to be removed
     * @return true if exists and is removed
     */
    public boolean removeSequenceDiagram(UMLSequenceDiagram sequenceDiagram){
        return sequenceDiagrams.remove(sequenceDiagram);
    }

    /**
     * Method which loads content of diagrams from file
     * @param filename name of file, from which diagrams will be loaded
     * @return UML object with all diagrams
     */
    static public UML loadDiagramsFromFile(String filename){
        //create gson
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();

        UML uml = new UML();
        try{
            BufferedReader br = new BufferedReader(new FileReader(filename));
            uml = gson.fromJson(br, UML.class); //load the content of json and store into objects
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        return uml;
    }

    /**
     * Method which stores UML objects (diagrams with content) into file
     * @param filename file into which diagrams will be saved (json)
     */
    public void storeDiagramsToFile(String filename){
        //create gson
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        try{
            FileWriter writer = new FileWriter(filename);
            writer.write(gson.toJson(this)); //writes diagrams into json
            writer.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //load and save data into objects, then write data from objects back into file
        UML umlDiagrams = UML.loadDiagramsFromFile("src/main/java/ija/app/uml/data_input.json");
        umlDiagrams.storeDiagramsToFile( "src/main/java/ija/app/uml/data_output.json");
    }

}
