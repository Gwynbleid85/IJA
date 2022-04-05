package ija.app.uml;
import java.util.*;

public class UMLOperation extends UMLAttribute {
	
	private List<UMLAttribute> args;

	public UMLOperation(java.lang.String name, UMLClassifier type){
		super(name, type);
		this.args = new ArrayList<UMLAttribute>();
	}


	public static UMLOperation create(java.lang.String name, UMLClassifier type, UMLAttribute... args){
		UMLOperation newOperation = new UMLOperation(name, type);
		for (UMLAttribute umlAttribute : args) {
			newOperation.addArgument(umlAttribute);
		}
		return newOperation;
	}

	public boolean addArgument(UMLAttribute arg){
		if(this.args.contains(arg))
			return false;
		if(this.args.add(arg))
			return true;
		return false;
	}

	public List<UMLAttribute> getArguments(){
		return Collections.unmodifiableList(this.args);
	}
}
