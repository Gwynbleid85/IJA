package ija.app.gui;

import com.sun.javafx.collections.ImmutableObservableList;
import ija.app.gui.dialogs.*;
import ija.app.history.History;
import ija.app.history.historyEvents.HE_addAndDelete_T;
import ija.app.history.historyEvents.HE_addNew;
import ija.app.history.historyEvents.HE_delete;
import ija.app.uml.UML;
import ija.app.uml.classDiagram.UMLClass;
import ija.app.uml.classDiagram.UMLClassDiagram;
import ija.app.uml.classDiagram.UMLRelation;
import ija.app.uml.sequenceDiagram.UMLSequenceDiagram;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class G_UMLClassDiagram implements HE_addAndDelete_T {
	/** UMLClassDiagram represented by this object */
	private UMLClassDiagram diagram;
	/** Parent of this graphical representation */
	private G_UML parent;
	/** Graphical representations of all UMLClasses */
	private List<G_UMLClass> classes;
	/** Graphical representations of all UMLRelations */
	private List<G_UMLRelation> relations;
	/** Selected object (G_UMLCLass or G_UMLRelation) */
	private G_selectable selected;
	/** Main node of this representation */
	private Group root;
	/** Scene of UMLClassDiagram */
	private Scene classDiagramScene;

	/**
	 * Constructor of graphical representation of UMLCLassDiagram
	 * @param diagram UMLClassDiagram to be represented
	 * @param parent Parent of this representation
	 * @throws IOException
	 */
	public G_UMLClassDiagram(UMLClassDiagram diagram, G_UML parent) throws IOException {
		this.diagram = diagram;
		this.parent = parent;
		selected = null;
		/* Prepare template */
		root = new Group();
		Group relationsGroup = new Group();
		relationsGroup.setId("relationsGroup");
		Group classesGroup = new Group();
		classesGroup.setId("classesGroup");
		root.getChildren().add(relationsGroup);
		root.getChildren().add(classesGroup);

		/* Create new Scene */
		Pane template = FXMLLoader.load(getClass().getResource("fxml/G_ClassDiagramSceene.fxml"));
		template.getChildren().add(root);
		classDiagramScene = new Scene(template);
		/* Bind history to this scene */
		History.getInstance(classDiagramScene);
		/* Set menu */
		setMenu();
		setEventHandlers();

		/* Create gui classes*/
		classes = new LinkedList<>();
		for(UMLClass c : diagram.getClasses()){
			G_UMLClass gc = new G_UMLClass(c, this);
			classes.add(gc);
			((Group)root.lookup("#classesGroup")).getChildren().add(gc.getNode());
		}
	}

	public void setMenu(){
		((MenuBar)classDiagramScene.lookup("#MenuBar")).getMenus().clear();
		/* Set file op buttons*/
		MenuItem load = new MenuItem("Load from file");
		MenuItem save = new MenuItem("Save to file");
		load.setOnAction( e -> {
			try {
				G_LoadFile dialog = new G_LoadFile(parent.getStage(), parent);
				UML newUML = dialog.showDialog();
				parent.changeUML(newUML);
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}

		});
		save.setOnAction( e -> {
			FileChooser fileChooser = new FileChooser();
			File file = fileChooser.showSaveDialog(parent.getStage());
			parent.getUml().storeDiagramsToFile(file.getAbsolutePath());
		});
		Menu fileMenu = new Menu("File");
		fileMenu.getItems().add(load);
		fileMenu.getItems().add(save);

		/* Set history buttons */
		MenuItem undo = new MenuItem("Undo (Ctrl+z)");
		MenuItem redo = new MenuItem("Redo (Ctrl+Shift+z)");
		undo.setOnAction( e -> {
			History.undoStatic();
		});
		redo.setOnAction( e -> {
			History.redoStatic();
		});
		Menu edit = new Menu("Edit");
		edit.getItems().add(undo);
		edit.getItems().add(redo);
		/* Add all to menuBar*/
		((MenuBar)classDiagramScene.lookup("#MenuBar")).getMenus().add(fileMenu);
		((MenuBar)classDiagramScene.lookup("#MenuBar")).getMenus().add(edit);

		/* Changing diagrams buttons */
		/* Class diagram button */
		classDiagramScene.lookup("#SetClassDiagram").setDisable(true);
		//
		classDiagramScene.lookup("#SetClassDiagram").setOnMouseClicked( e -> {
			parent.setScene(0, "");
			System.out.println("Changing to class diagram 11");

		});

		/* Sequence diagram button */
		List<String> asdf = new ArrayList<>();
		for(UMLSequenceDiagram s : parent.getSequenceDiagrams())
			asdf.add(s.getName());
		ObservableList<String> seqDiagrams = FXCollections.observableArrayList(asdf);
		((ComboBox<String>)classDiagramScene.lookup("#SetSeqDiagram")).setOnAction(e->{});
		((ComboBox<String>)classDiagramScene.lookup("#SetSeqDiagram")).setItems(seqDiagrams);
		((ComboBox<String>)classDiagramScene.lookup("#SetSeqDiagram")).promptTextProperty().set("Choose sequence diagram");
		((ComboBox<String>)classDiagramScene.lookup("#SetSeqDiagram")).setOnAction( e -> {
			parent.setScene(1, ((ComboBox<String>) classDiagramScene.lookup("#SetSeqDiagram")).getSelectionModel().getSelectedItem());
			System.out.println("Changing to class diagram 12");

		});

		((Button)classDiagramScene.lookup("#newSeqDia")).setOnAction( e -> {
			// New Sequence diagram
			try {
				G_NewSeqDiagram dialog = new G_NewSeqDiagram(parent.getStage(), parent);
				String newName = dialog.showDialog();
				parent.setScene(1, newName);
				System.out.println("Changing to class diagram 13");
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		});

	}

	public void draw(Stage stage) throws IOException {

		/* Check consistency of relations */
		G_UMLRelationConsistencyCheck consistencyCheck = new G_UMLRelationConsistencyCheck(this, stage);
		Set<UMLRelation> relationsCopy = new HashSet<>(diagram.getRelations());
		for(UMLRelation rel : relationsCopy){
			if(!consistencyCheck.checkConsistency(rel))
				diagram.delRelation(rel);
		}

		/* Create gui relations*/
		relations = new LinkedList<>();
		for(UMLRelation r : diagram.getRelations()){
			G_UMLRelation gr = new G_UMLRelation(r, this);
			relations.add(gr);
			((Group)root.lookup("#relationsGroup")).getChildren().add(gr.getNode());
		}
	}
	/**
	 * Method to get scene of UMLClassDiagram representation
	 * @return scene of UMLClassDiagram representation
	 */
	public Scene getScene(){
		return classDiagramScene;
	}

	/**
	 * Method for adding necessary eventHandlers
	 */
	private void setEventHandlers(){
		/* Delete selection on click */
		classDiagramScene.getRoot().setOnMouseClicked(e -> {
			if(e.isStillSincePress()){
				System.out.println("Clear select");
				setSelected(null);
			}
		});

		/* Delete selected object */
		classDiagramScene.setOnKeyPressed( e-> {
			if(selected != null && e.getCode() == KeyCode.DELETE){
				delete(selected);
			}
		});

		/* newRelation button*/
		classDiagramScene.getRoot().lookup("#newRelation").setOnMouseClicked(e->{
			/* make new relation */
			UMLRelation newRelation = new UMLRelation("New relation");
			G_EditUMLRelationDialog dialog = new G_EditUMLRelationDialog(newRelation, this, true);
			boolean isChanged;
			/* Edit new relation */
			try {
				isChanged = dialog.showDialog(parent.getStage());
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
			/* Add new class*/
			if(isChanged){
				System.out.println("isChanged");
				try {
					addGUMLRelation(new G_UMLRelation(newRelation, this));
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			}
		});

		/* newClass button*/
		classDiagramScene.getRoot().lookup("#newClass").setOnMouseClicked(e->{
			/* make new class */
			UMLClass newClass= new UMLClass("New class");
			G_EditUMLClassDialog dialog = new G_EditUMLClassDialog(newClass, this, true);
			boolean isChanged;
			/* Edit new class */
			try {
				isChanged = dialog.showDialog(parent.getStage());
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}

			/* Add new class*/
			if(isChanged){
				System.out.println("isChanged");
				try {
					addGUMLClass(new G_UMLClass(newClass, this));
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			}
		});

		/* Delete button */
		classDiagramScene.getRoot().lookup("#Delete").setOnMouseClicked(e->{
			if(selected != null)
				delete(selected);
		});

		/* edit button*/
		classDiagramScene.getRoot().lookup("#Edit").setOnMouseClicked(e->{
			if(Objects.equals(selected.getType(), "UMLClass")){
				UMLClass toChange = (UMLClass) ((G_UMLClass)selected).createCopy();
				String oldName = toChange.getName();;
				G_EditUMLClassDialog dialog = new G_EditUMLClassDialog(toChange, this, false);
				boolean isChanged;
				try {
					isChanged = dialog.showDialog(parent.getStage());
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
				if(isChanged){
					if(!Objects.equals(oldName, toChange.getName()))
						updatedClassName(oldName, toChange.getName());
					((G_UMLClass)selected).loadCopy(toChange);
					((G_UMLClass)selected).update();
				}
			}
			else{
				UMLRelation toChange = (UMLRelation) ((G_UMLRelation)selected).createCopy();
				G_EditUMLRelationDialog dialog = new G_EditUMLRelationDialog(toChange, this, false);
				boolean isChanged;
				try {
					isChanged = dialog.showDialog(parent.getStage());
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
				if(isChanged){
					((G_UMLRelation)selected).loadCopy(toChange);
					try {
						updateRelations();
					} catch (IOException ex) {
						throw new RuntimeException(ex);
					}
				}
			}
		});
	}

	/**
	 * Method to set selected objects
	 * @param selected Object that was selected
	 */
	public void setSelected(G_selectable selected){
		if(this.selected != null)
			this.selected.setSelect(false);
		this.selected = selected;
		/* set edit button*/
		classDiagramScene.getRoot().lookup("#Edit").setDisable(selected == null);
		classDiagramScene.getRoot().lookup("#Delete").setDisable(selected == null);
		/* Move class to the top */
		if(selected != null){
			if(Objects.equals(selected.getType(), "UMLClass")){
				Node c = ((G_UMLClass)selected).getNode();
				((Group)root.lookup("#classesGroup")).getChildren().remove(c);
				((Group)root.lookup("#classesGroup")).getChildren().add(c);
			}
		}
	}

	/**
	 * Method to get G_UMLClass by name
	 * @param name Name of class to be searched for
	 * @return Found class, otherwise null
	 */
	public G_UMLClass getGUMLClass(String name){
		for(G_UMLClass gc : classes){
			if(Objects.equals(gc.getName(), name)){
				return gc;
			}
		}
		return null;
	}

	/**
	 * Get list of all UMLClasses in UMLClassDiagram
	 * @return list of all UMLClasses in UMLClassDiagram
	 */
	public Set<UMLClass> getClasses(){
		return diagram.getClasses();
	}

	/**
	 * Method to add new UMLClass
	 * @param newClass New UMLClass to be added
	 * @throws IOException
	 */
	public void addUMLClass(UMLClass newClass) throws IOException {
		if(diagram.addClass(newClass)){
			G_UMLClass newGUMLClass = new G_UMLClass(newClass, this);
			classes.add(newGUMLClass);
			((Group)root.lookup("#classesGroup")).getChildren().add(newGUMLClass.getNode());
		}
	}

	/**
	 * Method to update class names in relations
	 * @param oldName Old name of class
	 * @param newName New name of class
	 */
	public void updatedClassName(String oldName, String newName){
		for(UMLRelation rel : diagram.getRelations()) {
			/* update to */
			if (Objects.equals(rel.getTo(), oldName))
				rel.setTo(newName);
			/* Update from*/
			if (Objects.equals(rel.getFrom(), oldName))
				rel.setFrom(newName);
		}
	}

	/**
	 * Method to add new selectable object
	 * @param addNew New selectable object
	 */
	public void addNew(G_selectable addNew){
		if(Objects.equals(addNew.getType(), "UMLClass"))
			addGUMLClass((G_UMLClass) addNew);
		else if(Objects.equals(addNew.getType(), "UMLRelation"))
			addGUMLRelation((G_UMLRelation) addNew);
		setSelected(null);
	}

	public void addGUMLClass(G_UMLClass newClass){
		System.out.println("Add class");
		/* Add deletion to history */
		History.addEvent(new HE_addNew(this, newClass));
		/* Add class */
		newClass.setSelect(false);
		classes.add(newClass);
		((Group)root.lookup("#classesGroup")).getChildren().add(newClass.getNode());
		diagram.addClass(newClass.getUmlClass());
	}

	public void addGUMLRelation(G_UMLRelation newRel){
		System.out.println("Add relation");
		/* Add deletion to history */
		History.addEvent(new HE_addNew(this, newRel));
		/* Add relation */
		newRel.setSelect(false);
		relations.add(newRel);
		((Group)root.lookup("#relationsGroup")).getChildren().add(newRel.getNode());
		diagram.addRelation(newRel.getUMLRelation());
	}

	/**
	 * Method to deleted selected object
	 * @param toDel Selected object to delete
	 */
	public void delete(G_selectable toDel){
		if(Objects.equals(toDel.getType(), "UMLClass"))
			delClass((G_UMLClass) toDel);
		else if(Objects.equals(toDel.getType(), "UMLRelation"))
			delRelation((G_UMLRelation) toDel);
		setSelected(null);
	}

	/**
	 * Method to remove G_UMLClass from G_UMLClassDiagram
	 * @param toDel G_UMLClass to be deleted
	 * @return true if deleted successfully
	 */
	public boolean delClass(G_UMLClass toDel){
		System.out.println("Delete class");
		/* Add deletion to history */
		History.addEvent(new HE_delete(this, toDel));
		/* remove class from relations */
		for(int i = 0; i<relations.size(); i++){
			G_UMLRelation rel = relations.get(i);
			if( Objects.equals(rel.getUMLRelation().getTo(), toDel.getName()) ||
				Objects.equals(rel.getUMLRelation().getFrom(), toDel.getName())){
				if(delRelation(rel)){
					System.out.println("Deleted: " + rel.getUMLRelation().getName());
					i--;
				}
			}
		}
		/* Delete class */
		((Group)root.lookup("#classesGroup")).getChildren().remove(toDel.getNode());
		classes.remove(toDel);
		try{updateRelations();}
		catch(Exception ignored){}
		return diagram.delClass(toDel.getName());
	}

	/**
	 * Method to remove G_UMLRelation from G_UMLClassDiagram
	 * @param toDel G_UMLRelation to be deleted
	 * @return true if deleted successfully
	 */
	public boolean delRelation(G_UMLRelation toDel){
		System.out.println("Delete relation");
		/* Add deletion to history */
		History.addEvent(new HE_delete(this, toDel));
		/* Delete relation */
		((Group)root.lookup("#relationsGroup")).getChildren().remove(toDel.getNode());
		relations.remove(toDel);
		return diagram.delRelation(toDel.getUMLRelation());
	}

	/**
	 * Method to update graphical representation of UMLRelations
	 */
	public void updateRelations() throws IOException {
		for(G_UMLRelation r : relations){
			r.update();
		}
	}


}
