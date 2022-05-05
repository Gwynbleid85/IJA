package ija.app.uml.classDiagram;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Milos Hegr (xhegrm00)
 * @date 10.4.2022
 * Class representing relations between classes in UML class diagram
 */

public class UMLRelation{

	private String name;
	private String type;
	private String to;
	private String from;
	private String cardinalityTo;
	private String cardinalityFrom;

	/**
	 * Constructor of UMLRelation
	 * @param type Type of created relation
	 */
	public UMLRelation(String type){
		this.type = type;
		from = null;
		to = null;
		name = "";
		cardinalityTo = "";
		cardinalityFrom = "";
	}

	//public UMLRelation(String type){
	//	this.type = type;
	//	this.classes = null;
	//	from = new LinkedList<>();
	//	to = null;
	//	name = "";
	//}

	/**
	 * Getter of type attribute
	 * @return New value of type attribute
	 */
	public String getType(){
		return type;
	}

	/**
	 * Setter of type attribute
	 * Possible types (Association, Aggregation, Composition and Generalization)
	 * @param type New value of type attribute
	 */
	public void setType(String type){
		this.type = type;
	}

	/**
	 * Getter of name attribute
	 * @return Value of name attribute
	 */
	public String getName(){
		return name;
	}

	/**
	 * Setter of name attribute
	 * @param name New value of name attribute
	 */
	public void setName(String name){
		this.name = name;
	}

	/**
	 * Getter of to attribute
	 * @return Value of to attribute
	 */
	public String getTo(){
		return to;
	}

	/**
	 * Setter of to attribute
	 * @param to New value for to attribute
	 */
	public void setTo(String to){
		this.to = to;
	}

	/**
	 * Getter of from attribute
	 * @return Unmodifiable list from
	 */
	public String getFrom(){
		return from;
	}

	/**
	 * Method to add element to this.from list
	 * @param from New from value
	 */
	public void setFrom(String from){
		this.from = from;
	}
	public String getCardinalityTo() {
		return cardinalityTo;
	}

	public void setCardinalityTo(String cardinalityTo) {
		this.cardinalityTo = cardinalityTo;
	}

	public String getCardinalityFrom() {
		return cardinalityFrom;
	}

	public void setCardinalityFrom(String cardinalityFrom) {
		this.cardinalityFrom = cardinalityFrom;
	}


	/**
	 * Method for checking consistency of UMLDiagrams
	 * @return True if UMLDiagram is consistent
	 */
	public boolean consistencyCheck(Set<UMLClass> classes) {
		/*Check if to class exists*/
		if( ! classes.contains(new UMLClass(to)))
			return false;
		/*Check if all from classes exists*/
		if( ! classes.contains(new UMLClass(from)))
			return false;

		return true;
	}
}
