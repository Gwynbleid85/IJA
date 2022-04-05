package ija.app.uml;

public class Element {

	protected java.lang.String name;

	public Element(java.lang.String name) {
		this.name = name;
	}

	public java.lang.String getName(){
		return this.name;
	}

	public void rename(java.lang.String name) {
		this.name = name;
	}
	
}
