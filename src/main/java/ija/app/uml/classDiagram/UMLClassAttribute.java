package ija.app.uml.classDiagram;


/**
 * @author Milos Hegr (xhegrm00)
 * @date 9.4.2022
 * Class representing attribute of UML class
 */

public class UMLClassAttribute {
	private String name;
	private String datatype;
	private String accessMod;

	public UMLClassAttribute(String name, String datatype, String accessMod){
		this.name = name;
		this.datatype = datatype;
		this.accessMod = accessMod;
	}
	public UMLClassAttribute(String name){
		this.name = name;
		this.datatype = "";
		this.accessMod = "";
	}
	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getDatatype(){
		return datatype;
	}

	public void setDatatype(String datatype){
		this.datatype = datatype;
	}

	public String getAccessMod(){
		return accessMod;
	}

	public void setAccessMod(String accessMod){
		this.accessMod = accessMod;
	}

	@Override
	public String toString() {
		return accessMod + " " + name + ": " + datatype;
	}

	@Override
	public boolean equals(Object o) {
		if(! (o instanceof UMLClassAttribute)) return false;

		UMLClassAttribute c = (UMLClassAttribute) o;
		return this.name.equals(c.name);
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
}
