package ija.app.uml.classDiagram;

public class UMLClassAttribute {
	private String name;
	private String datatype;
	private String accessMod;

	public UMLClassAttribute(String name, String datatype, String accessMod){
		this.name = name;
		this.datatype = datatype;
		this.accessMod = accessMod;
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
}
