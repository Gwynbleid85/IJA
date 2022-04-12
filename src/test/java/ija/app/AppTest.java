/*
 * IJA 2021/22: Úloha č. 1
 * Testovací třída pro JUnit.
 * (C) rk
 */
package ija.app;

import ija.app.uml.classDiagram.*;
import ija.app.uml.sequenceDiagram.*;
import java.util.List;

import org.junit.Test;
import org.junit.Assert;


public class AppTest {

	@Test
	public void testUMLClassInstance(){
		ClassDiagram cd = new ClassDiagram(); //todo
		//TODO vytvoreni trid, pridani do diagramu
		//TODO vytvoreni UMLClassInstance



	}

	@Test
	public void testUMLMessage(){
		ClassDiagram cd = new ClassDiagram(); //todo
		//TODO vytvoreni trid, pridani do diagramu
		UMLMessage msg = new UMLMessage(cd, "class1", "class2", "Count max");

		Assert.assertEquals("Test UMLMessage value", msg.getMessage(), "Count max");
		msg.setMessage("Count min");
		Assert.assertEquals("Test UMLMessage new value", msg.getMessage(), "Count min");
		Assert.assertEquals("Test UMLMessage 'from' class", msg.getFrom(), "class1");
		msg.setFrom("Car");
		Assert.assertEquals("Test UMLMessage new 'from' class", msg.getFrom(), "Car");
		Assert.assertEquals("Test UMLMessage 'to' class", msg.getTo(), "class2");
		msg.setTo("Bus");
		Assert.assertEquals("Test UMLMessage new 'to' class", msg.getTo(), "Bus");


		//TODO create new message with a class that doesnt exist
	}

	@Test
	public void testSequenceDiagram() {
		SequenceDiagram sd = new SequenceDiagram("sd1");
		Assert.assertEquals("Test sequence diagram name", sd.getName(), "sd1");
		sd.setName("sd2");
		Assert.assertEquals("Test sequence diagram new name", sd.getName(), "sd2");
	}


}
