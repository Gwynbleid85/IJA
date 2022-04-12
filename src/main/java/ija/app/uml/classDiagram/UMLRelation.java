package ija.app.uml.classDiagram;

import java.util.*;
import java.util.stream.Collectors;

public class UMLRelation {

	private String name;
	private String type;
	private String to;
	private List<String> from;

	/**
	 * Constructor of UMLRelation
	 * @param type Type of created relation
	 */
	public UMLRelation(String type){
		this.type = type;
		from = new LinkedList<String>();
		to = null;
		name = "";
	}

	/**
	 * Getter of type attribute
	 * @return New value of type attribute
	 */
	public String getType(){
		return type;
	}

	/**
	 * Setter of type attribute
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
	public List<String> getFrom(){
		return Collections.unmodifiableList(from);
	}

	/**
	 * Method to add element to this.from list
	 * @param from New element to be added to this.from list
	 * @return False if given element already in this.from list, True otherwise
	 */
	public boolean setFrom(String from){
		if(! this.from.contains(from))
			return false;
		return this.from.add(from);
	}

	/**
	 * Appends list of String to this.from
	 * @param from List to be appended
	 * @return False if some element from given list exists in this.from, True otherwise
	 */
	public boolean setFrom(List<String> from){
		/* Make intersect of this.from and from*/
		Set<String> result = from.stream()
				.distinct()
				.filter(this.from::contains)
				.collect(Collectors.toSet());
		/*If intersect not empty*/
		if(!result.isEmpty())
			return false;
		return this.from.addAll(from);
	}
}
