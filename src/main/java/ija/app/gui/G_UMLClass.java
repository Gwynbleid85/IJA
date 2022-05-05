package ija.app.gui;

import ija.app.history.History;
import ija.app.history.historyEvents.HE_edit;
import ija.app.history.historyEvents.HE_edit_T;
import ija.app.history.historyEvents.HE_move;
import ija.app.history.historyEvents.HE_move_T;
import ija.app.uml.classDiagram.UMLClass;
import ija.app.uml.classDiagram.UMLClassAttribute;
import ija.app.uml.classDiagram.UMLClassMethod;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class G_UMLClass implements G_selectable, HE_move_T, HE_edit_T {
	private G_UMLClassDiagram parent;
	private UMLClass umlClass;
	private VBox GumlClass;

	private G_Position originPos;


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
		Random r = new Random();
		System.out.println(d.getScene().getWidth());
		GumlClass.setLayoutX(50 + (Math.abs(r.nextInt()) % 300));
		GumlClass.setLayoutY(50 + (Math.abs(r.nextInt()) % 300));
		originPos = null;
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
				/* Save original position */
				if(originPos == null)
					originPos = new G_Position(GumlClass.getLayoutX(), GumlClass.getLayoutY());
				GumlClass.setLayoutX(Math.min(Math.max(e.getSceneX() - GumlClass.getWidth()/2, 0), GumlClass.getScene().getWidth()- GumlClass.getWidth()));
				GumlClass.setLayoutY(Math.min(Math.max(e.getSceneY() - GumlClass.getHeight()/2, 100), GumlClass.getScene().getHeight()-GumlClass.getHeight()));
				try {
					parent.updateRelations();
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			}
		});

		/* Detect end of drag */
		GumlClass.setOnMouseReleased( e -> {
			if(originPos != null){
				History.addEvent(new HE_move(this, originPos));
				originPos = null;
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

	public void moveTo(G_Position pos) throws IOException {
		History.addEvent(new HE_move(this, new G_Position(GumlClass.getLayoutX(), GumlClass.getLayoutY())));
		GumlClass.setLayoutX(pos.x);
		GumlClass.setLayoutY(pos.y);
		parent.updateRelations();
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

	/**
	 * Get width and height of G_UMLClass
	 * @return width and height of G_UMLClass
	 */
	public G_Position getBorders(){
		return new G_Position(GumlClass.getWidth(), GumlClass.getHeight());
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

	@Override
	public void loadCopy(Object copy) {
		/* Add edit to history */
		History.addEvent(new HE_edit(this, createCopy()));
		/* Load copy */
		UMLClass toCopy = (UMLClass) copy;
		/* Update relations */
		if(!Objects.equals(toCopy.getName(), umlClass.getName()))
			parent.updatedClassName(umlClass.getName(), toCopy.getName());
		/* Copy name */
		umlClass.setName(toCopy.getName());
		/* Copy is interface */
		umlClass.setIsInterface(toCopy.isInterface());
		/* Copy attributes */
		umlClass.delAttributes();
		for(UMLClassAttribute a : toCopy.getAttributes())
			umlClass.addAttribute(a);
		/* Copy methods */
		umlClass.delMethods();
		for(UMLClassMethod m : toCopy.getMethods())
			umlClass.addMethod(m);
		/* Update graphics representation*/
		update();
	}

	@Override
	public Object createCopy() {
		/* Create copy */
		UMLClass copy = new UMLClass(umlClass.getName(), umlClass.isInterface());
		/* Copy attributes */
		for(UMLClassAttribute a : umlClass.getAttributes())
			copy.addAttribute(a);
		/* Copy methods*/
		for(UMLClassMethod m : umlClass.getMethods())
			copy.addMethod(m);

		return copy;
	}
}
