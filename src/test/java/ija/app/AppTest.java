/*
 * IJA 2021/22: Úloha č. 1
 * Testovací třída pro JUnit.
 * (C) rk
 */
package ija.app;

import ija.app.uml.classDiagram.*;
import ija.app.uml.sequenceDiagram.*;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import ija.app.uml.classDiagram.*;
import org.junit.Assert;

/**
 * todo: rewrite
 * Testovací třída pro první úkol z předmětu IJA (2021/22).
 * Testovací případy nemusí pokrývat celou funkcionalitu požadovanou API a kontrakty metod v zadání.
 * @author koci
 */
public class AppTest {
	/**
	 * Test UMLClassAttribute
	 */
	@Test
	public void testUMLClassAttribute(){
		UMLClassAttribute a = new UMLClassAttribute("name", "type", "accessor");
		/*Test changing value of attributes*/
		Assert.assertEquals("Test name", a.getName(), "name");
		Assert.assertEquals("Test datatype", a.getDatatype(), "type");
		Assert.assertEquals("Test AccessMod", a.getAccessMod(), "accessor");
		a.setName("name2");
		Assert.assertEquals("Test changed name", a.getName(), "name2");
		a.setAccessMod("asdf");
		Assert.assertEquals("Test changed accessMod", a.getAccessMod(), "asdf");
		/*Test equality of attributes*/
		UMLClassAttribute b = new UMLClassAttribute("name2", "type2", "access");
		Assert.assertEquals("Test comparing", a, b);

	}

	/**
	 * Test UMLMethod
	 */
	@Test
	public void testUMLMethod(){
		UMLClassMethod m = new UMLClassMethod("method", "+");
		Assert.assertEquals("Test name", m.getName(),"method");
		Assert.assertEquals("Test accessMod", m.getAccessMod(), "+");

		m.setName("method2");
		Assert.assertEquals("Test changed name", m.getName(), "method2");
		m.setAccessMod("-");
		Assert.assertEquals("Test changed accessMod", m.getAccessMod(), "-");

		Assert.assertEquals("Test overwritten method equals", m, new UMLClassMethod("method2"));
	}

	/**
	 * Test UMLClass
	 */
	@Test
	public void testUMLClass() {
		UMLClass c = new UMLClass("class");

		Assert.assertFalse("Test interface attrib", c.isInterface());
		c.setIsInterface(true);
		Assert.assertTrue("Test changed interface attrib", c.isInterface());

		/*Test UMLAttributes*/
		Assert.assertTrue("Test add new attribute", c.addAttribute(new UMLClassAttribute("attrib1", "asdf", "asdf")));
		Assert.assertFalse("Test adding existing attribute", c.addAttribute(new UMLClassAttribute("attrib1", "lkjh", "lkjh")));

		for (UMLClassAttribute attrib : c.getAttributes())
			Assert.assertEquals("Check inserted attributes", attrib, new UMLClassAttribute("attrib1", "asdf", "asdf"));

		Assert.assertTrue("Remove existing attribute", c.delAttribute("attrib1"));
		Assert.assertFalse("Remove non existing attribute", c.delAttribute("attrib1"));

		/*Test UMLMethods*/
		Assert.assertTrue("Test add new method", c.addMethod(new UMLClassMethod("method1")));
		Assert.assertFalse("Test add existing method", c.addMethod(new UMLClassMethod("method1")));

		for(UMLClassMethod method : c.getMethods())
			Assert.assertEquals("Check inserted methods", method, new UMLClassMethod("method1"));

		Assert.assertTrue("Remove existing method", c.delMethod("method1"));
		Assert.assertFalse("Remove non existing method", c.delMethod("method1"));
	}

	/**
	 * Test UMLrelations
	 */
	@Test
	public void testUMLRelation(){
		UMLRelation u = new UMLRelation("asdf");
	}


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
