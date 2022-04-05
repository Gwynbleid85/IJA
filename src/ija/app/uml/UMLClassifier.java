package ija.app.uml;


public class UMLClassifier extends Element{
	
	protected boolean isUserDefined;

	public UMLClassifier(java.lang.String name) {
		super(name);
		this.isUserDefined = true;
	}
	
	public UMLClassifier(java.lang.String name, boolean isUserDefined) {
		super(name);
		this.isUserDefined = isUserDefined;
	}

	public static UMLClassifier forName (java.lang.String name) {
		return new UMLClassifier(name, false);
	}

	public boolean isUserDefined(){
		return this.isUserDefined;
	}

	public java.lang.String toString() {
		return super.name+'('+this.isUserDefined+')';
	}



	//Equals
	public boolean equals(Object o) {
		if(! (o instanceof UMLClassifier)) return false;

		UMLClassifier c = (UMLClassifier) o;
		return this.name.equals(c.name);
	}

	public int hashCode() {
		return this.name.hashCode();
	}
}

