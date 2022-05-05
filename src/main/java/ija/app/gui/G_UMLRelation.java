package ija.app.gui;

import ija.app.history.historyEvents.HE_edit_T;
import ija.app.uml.classDiagram.UMLRelation;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;

import java.io.IOException;
import java.util.Objects;

public class G_UMLRelation implements G_selectable, HE_edit_T {
	private UMLRelation relation;
	private G_UMLClassDiagram parent;
	private Group root;
	public G_UMLRelation(UMLRelation r, G_UMLClassDiagram d) throws IOException {
		relation = r;
		parent = d;
		root = new Group();
		setEventHandlers();
		update();
	}

	/**
	 * Method creates necessary event handlers
	 */
	private void setEventHandlers() {
		root.setOnMouseClicked( e -> {
			if(e.isStillSincePress()){
				System.out.println("Select " + relation.getName());
				setSelect(true);
				e.consume();
			}
		});
	}

	public void update() throws IOException {
		/* Get positions of UMCLasses */
		G_Position from;
		G_Position to;
		G_Position toBorder;
		try{
			from = parent.getGUMLClass(relation.getFrom()).getPos();
			to = parent.getGUMLClass(relation.getTo()).getPos();
			toBorder = parent.getGUMLClass(relation.getTo()).getBorders();
		}
		catch (Exception e){
			from = new G_Position(0, 0);
			to = new G_Position(0, 0);
			toBorder = new G_Position(0, 0);
		}
		/* Count angle */
		double angle = Math.toDegrees(Math.atan(Math.abs(from.x - to.x)/Math.abs(from.y- to.y)));
		if((from.x-to.x)*(from.y-to.y) < 0)
			angle *= -1;
		if(from.y < to.y)
			angle += 180;
		angle += 90;

		/* get intersection of relation line and To UMLClass */
		double tan;
		double tan2;
		if(angle < 180)
			tan = Math.tan(Math.toRadians(360 - angle));
		else
			tan = Math.tan(Math.toRadians(angle));

		if(angle < 90 || angle > 270)
			tan2 = Math.tan(Math.toRadians(360 - angle + 90));
		else
			tan2 = Math.tan(Math.toRadians(angle+90));
		G_Position intersection = new G_Position(
				/* X */ 	Math.min(Math.max(to.x + (toBorder.y/2)/ tan, to.x - toBorder.x/2), to.x + toBorder.x/2),
				/* Y */ 	Math.min(Math.max(to.y + (toBorder.x/2)/ tan2, to.y - toBorder.y/2), to.y + toBorder.y/2)
		);
		/* Create relation line */
		Line line = new Line();
		line.setId("Line");
		line.setStrokeWidth(3);
		/* Set line */
		line.setStartX(from.x);
		line.setStartY(from.y);
		line.setEndX(intersection.x);
		line.setEndY(intersection.y);


		/* Customize line end arrow base on relation type*/
		Polygon end = new Polygon(0, 0);
		if(Objects.equals(relation.getType(), "Aggregation")){
			end = new Polygon(0, 0, 15, 5, 20, 20, 5, 15);
			end.setFill(Color.WHITE);
		}
		else if(Objects.equals(relation.getType(), "Composition")){
			end = new Polygon(0, 0, 15, 5, 20, 20, 5, 15);
			end.setFill(Color.BLACK);
		}
		else if(Objects.equals(relation.getType(), "Generalization")){
			end= new Polygon(0, 0, 15, 5, 5, 15);
			end.setFill(Color.WHITE);
		}
		end.setStroke(Color.BLACK);
		end.setStrokeWidth(2);
		end.setId("end");
		/* Rotate and move line end */
		Rotate rotate = new Rotate();
		rotate.setAngle(- angle + 135);
		end.getTransforms().add(rotate);
		end.setLayoutX(intersection.x);
		end.setLayoutY(intersection.y);

		/* Add objects to root */
		root.getChildren().clear();
		root.getChildren().addAll(line, end);
	}

	public UMLRelation getUMLRelation(){
		return relation;
	}
	public Node getNode(){return root;}

	@Override
	public void setSelect(boolean selected) {
		if(selected){
			parent.setSelected(this);
			((Line)root.lookup("#Line")).setStroke(Color.GRAY);
			((Polygon)root.lookup("#end")).setStroke(Color.GRAY);
		}
		else{
			((Line)root.lookup("#Line")).setStroke(Color.BLACK);
			((Polygon)root.lookup("#end")).setStroke(Color.BLACK);
		}
	}

	@Override
	public String getType() {
		return "UMLRelation";
	}

	@Override
	public void loadCopy(Object copy) {
		UMLRelation toCopy = (UMLRelation) copy;
		relation.setName(toCopy.getName());
		relation.setType(toCopy.getType());
		relation.setTo(toCopy.getTo());
		relation.setFrom(toCopy.getFrom());
		relation.setCardinalityTo(toCopy.getCardinalityTo());
		relation.setCardinalityFrom(toCopy.getCardinalityFrom());
	}

	@Override
	public Object createCopy() {
		UMLRelation copy = new UMLRelation(relation.getName());
		copy.setName(relation.getName());
		copy.setType(relation.getType());
		copy.setTo(relation.getTo());
		copy.setFrom(relation.getFrom());
		copy.setCardinalityTo(relation.getCardinalityTo());
		copy.setCardinalityFrom(relation.getCardinalityFrom());
		return copy;
	}
}
