package ija.app.gui;

import ija.app.uml.classDiagram.UMLClass;
import ija.app.uml.classDiagram.UMLClassAttribute;
import ija.app.uml.classDiagram.UMLClassMethod;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class G_UMLClass implements G_selectable{
	private G_UMLClassDiagram parent;
	private UMLClass umlClass;
	private VBox GumlClass;


	/**
	 * Consturcot of G_UMLClass
	 * @param c UMLClass to be graficaly represented
	 * @param d parent
	 * @throws IOException
	 */
	public G_UMLClass(UMLClass c, G_UMLClassDiagram d) throws IOException {
		umlClass = c;
		parent = d;
		GumlClass = FXMLLoader.load(getClass().getResource("fxml/G_UMLClass.fxml"));

		setEventHandlers();
		update();
	}

	/**
	 * Method creates necessary event handlers
	 */
	private void setEventHandlers() {

		/* Dragging UMLClass*/
		GumlClass.setOnMouseDragged(e -> {
			if(!e.isStillSincePress()){
				GumlClass.setLayoutX(e.getSceneX() - GumlClass.getWidth()/2);
				GumlClass.setLayoutY(e.getSceneY() - GumlClass.getHeight()/2);
				try {
					parent.updateRelations();
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			}
		});

		/* Selecting UMLClass*/
		GumlClass.setOnMouseClicked(e -> {
			if(e.isStillSincePress()){
				System.out.println("Select " + umlClass.getName());
				setSelect(true);
				e.consume();
			}
		});
	}

	/**
	 * Return root node of UMLClass representation
	 * @return Root node of UMLClass representation
	 */
	public Node getNode(){
		return GumlClass;
	}

	/**
	 * Get name of represented UMLClass
	 * @return Name of represented UMLClass
	 */
	public String getName() { return umlClass.getName(); }

	/**
	 * Get represented UMLClass
	 * @return Get represented UMLClass
	 */
	public UMLClass getUmlClass(){ return umlClass;}
	/**
	 * Update UMLClass texts
	 */
	public void update() {
		// Update name
		((Label)GumlClass.lookup("#umlClassName")).setText(umlClass.getName());

		// Update isInterface
		Label label = new Label("<<interface>>");
		label.setId("isInterface");
		/* Remove old isInterface label */
		if(!umlClass.isInterface() && GumlClass.lookup("#isInterface") != null)
			GumlClass.getChildren().remove(1);

		/* Add new isInterface label */
		if(umlClass.isInterface() && GumlClass.lookup("#isInterface") == null){
			GumlClass.getChildren().add(1, label);
		}

		//Update attributes
		VBox attribs = (VBox) GumlClass.lookup("#umlClassAttribs");
		attribs.getChildren().clear();
		for(UMLClassAttribute a : umlClass.getAttributes()){
			Label att = new Label(a.toString());
			//Todo set css
			//att.setFont(new Font(14));
			attribs.getChildren().add(att);
		}

		//Update methods
		VBox methods = (VBox) GumlClass.lookup("#umlClassMethods");
		methods.getChildren().clear();
		for(UMLClassMethod a : umlClass.getMethods()){
			Label met = new Label(a.toString());
			//Todo set css
			//met.setFont(new Font(14));
			methods.getChildren().add(met);
		}
	}

	/**
	 * Get middle position of represented UMLClass
	 * @return Middle position of represented UMLClass
	 */
	public G_Position getPos(){
		return new G_Position(GumlClass.getLayoutX() +GumlClass.getWidth()/2, GumlClass.getLayoutY() + GumlClass.getHeight()/2);
	}


	@Override
	public void setSelect(boolean selected) {
		if(selected){
			parent.setSelected(this);
			GumlClass.setStyle( "-fx-background-color : lightgray; " +
								"-fx-border-color : black; -fx-border-width : 2; ");
		}
		else{
			GumlClass.setStyle( "-fx-background-color : white; -fx-border-color : black;" +
								" -fx-border-width : 2 ;");
		}
	}

	@Override
	public String getType() {
		return "UMLClass";
	}
}
