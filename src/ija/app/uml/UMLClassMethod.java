package ija.app.uml;

public class UMLClassMethod {
	private String name;
	private String accessMod;

	public UMLClassMethod(String name, String accessMod){
		this.name = name;
		this.accessMod = accessMod;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getAccessMod(){
		return accessMod;
	}

	public void setAccessMod(String accessMod){
		this.accessMod = accessMod;
	}
}