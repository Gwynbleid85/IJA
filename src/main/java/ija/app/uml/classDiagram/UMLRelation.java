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

	public String getType(){
		return type;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getTo(){
		return to;
	}

	public void setTo(String to){
		this.to = to;
	}

	public String getFrom(){
		return from;
	}

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
		return classes.contains(new UMLClass(from));
	}
}
