package ija.app.gui;

import ija.app.gui.dialogs.G_EditUMLClassDialog;
import ija.app.uml.classDiagram.UMLClass;
import ija.app.uml.classDiagram.UMLClassDiagram;
import ija.app.uml.classDiagram.UMLRelation;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class G_UMLClassDiagram {
	/** UMLClassDiagram represented by this object */
	private UMLClassDiagram diagram;
	/** Parent of this graphical representation */
	private G_UML parent;
	/** Graphical representations of all UMLClasses */
	private Set<G_UMLClass> classes;
	/** Graphical representations of all UMLRelations */
	private Set<G_UMLRelation> relations;
	/** Selected object (G_UMLCLass or G_UMLRelation) */
	private G_selectable selected;
	/** Main node of this representation */
	private Group root;
	/** Sceen of UMLClassDiagram */
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
		root = new Group();

		/* Create new Scene */
		Pane template = FXMLLoader.load(getClass().getResource("fxml/G_ClassDiagramSceene.fxml"));
		template.getChildren().add(root);
		classDiagramScene = new Scene(template);

		/* Create gui classes*/
		classes = new HashSet<>();
		for(UMLClass c : diagram.getClasses()){
			G_UMLClass gc = new G_UMLClass(c, this);
			classes.add(gc);
			root.getChildren().add(gc.getNode());
		}

		/* Create gui relations*/
		relations = new HashSet<>();
		for(UMLRelation r : diagram.getRelations()){
			G_UMLRelation gr = new G_UMLRelation(r, this);
			relations.add(gr);
			root.getChildren().add(gr.getNode());
		}
		setEventHandlers();
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
				if(selected != null)
					selected.setSelect(false);
				selected = null;
				classDiagramScene.getRoot().lookup("#Edit").setDisable(true);

			}
		});

		/* newRelation button*/
		classDiagramScene.getRoot().lookup("#newRelation").setOnMouseClicked(e->{
			System.out.println("New relation");
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
					addUMLClass(newClass);
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			}

		});

		/* edit button*/
		classDiagramScene.getRoot().lookup("#Edit").setOnMouseClicked(e->{
			if(Objects.equals(selected.getType(), "UMLClass")){
				G_UMLClass toChange = ((G_UMLClass)selected);
				String oldName = toChange.getName();;
				G_EditUMLClassDialog dialog = new G_EditUMLClassDialog(toChange.getUmlClass(), this, false);
				boolean isChanged;
				try {
					isChanged = dialog.showDialog(parent.getStage());
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
				if(isChanged){
					if(!Objects.equals(oldName, toChange.getName()))
						updatedClassName(oldName, toChange.getName());
					toChange.update();
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
		classDiagramScene.getRoot().lookup("#Edit").setDisable(false);
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
		G_UMLClass newGUMLClass = new G_UMLClass(newClass, this);
		diagram.addClass(newClass);
		classes.add(newGUMLClass);
		root.getChildren().add(newGUMLClass.getNode());
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
			if(rel.delFrom(oldName))
				rel.addToFrom(newName);
		}
	}

	/**
	 * Method to remove UMLRelation from UMLClassDiagram
	 * @param toDel UMLRelation to be deleted
	 * @return true if deleted successfully
	 */
	public boolean delRelation(UMLRelation toDel, Node root){
		this.root.getChildren().remove(root);
		return diagram.delRelation(toDel);
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
