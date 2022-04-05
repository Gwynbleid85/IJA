package ija.app.uml;
import java.util.*;

public class UMLClass extends UMLClassifier {

	private boolean isAbstract;
	private List<UMLAttribute> attributes;

	public UMLClass(java.lang.String name) {
		super(name);
		super.isUserDefined = true;
		this.isAbstract = false;
		this.attributes = new ArrayList<UMLAttribute>();
	}

	public boolean isAbstract(){
		return this.isAbstract;
	}

	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	public boolean addAttribute(UMLAttribute attr) {
		if(!this.attributes.contains(attr)){
			this.attributes.add(attr);
			return true;
		}
		return false;
	}

	public int getAttrPosition(UMLAttribute attr) {
		if(!this.attributes.contains(attr))
			return -1;
		return this.attributes.indexOf(attr);
	}

	public int moveAttrAtPosition(UMLAttribute attr, int pos) {
		if(!this.attributes.contains(attr))
			return -1;
		int index = this.attributes.indexOf(attr);
		UMLAttribute tmp = this.attributes.get(index);
		this.attributes.remove(index);
		this.attributes.add(pos, tmp);
		return pos;
	}

	public List<UMLAttribute> getAttributes(){
		return Collections.unmodifiableList(this.attributes);
	}
}
