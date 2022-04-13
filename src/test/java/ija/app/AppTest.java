package ija.app;

import com.sun.source.tree.AssertTree;
import ija.app.uml.classDiagram.*;
import ija.app.uml.sequenceDiagram.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Test class for IJA project - homework3.
 * @author Milos Hegr (xhegrm00), Jiri Mladek (xmlade01)
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
	 * Test UMLRelation
	 */
	@Test
	public void testUMLRelation(){
		UMLRelation u = new UMLRelation("asdf", null);
		Assert.assertEquals("Test empty from list", u.getFrom(), new LinkedList<String>());
		Assert.assertNull("Test empty to attribute", u.getTo());

		Assert.assertTrue("Test insert new element from", u.addToFrom("1"));
		Assert.assertFalse("Test insert existing element to from", u.addToFrom("1"));

		Assert.assertTrue("Test insert list of new elements to from", u.addToFrom(new LinkedList<>(Arrays.asList("2", "3", "4"))));
		Assert.assertFalse("Test insert list of existing elements to from", u.addToFrom(new LinkedList<>(Arrays.asList("3", "4", "5"))));

		Assert.assertTrue("Test delete existing element from from", u.delFrom("3"));
		Assert.assertFalse("Test if element was deleted", u.getFrom().contains("3"));

		Assert.assertFalse("Test delete non existing element from from", u.delFrom("100"));

	}

	/**
	 * Test UMLClassDiagram
	 */
	@Test
	public void testUMLClassDiagram(){
		UMLClassDiagram d = new UMLClassDiagram();

		Assert.assertTrue("Test insert new UMLClass", d.addClass(new UMLClass("a")));
		Assert.assertFalse("Test insert existing UMLClass", d.addClass(new UMLClass("a")));
		Assert.assertTrue("Test remove existing UMLClass", d.delClass("a"));
		Assert.assertFalse("Test remove non existing UMLClass", d.delClass("a"));
		Assert.assertTrue("Test insert new UMLRelation", d.addRelation(new UMLRelation("b", null)));

		UMLClass a = new UMLClass("a");
		a.addAttribute(new UMLClassAttribute("a"));
		a.addMethod(new UMLClassMethod("aa"));

		UMLClass b = new UMLClass("b");
		b.addAttribute(new UMLClassAttribute("b"));
		b.addMethod(new UMLClassMethod("bb"));

		UMLRelation r = new UMLRelation("in", d.getClasses());
		r.setTo(b.getName());
		r.addToFrom(a.getName());

		d.addClass(a);
		d.addClass(b);

		d.addRelation(r);
		List<UMLClassMethod> ms = d.getUMLClassInheritedMethods("a");
		System.out.println(ms);
		Assert.assertTrue("Test get own methods 1", d.getUMLClassOwnMethods("a").contains(new UMLClassMethod("aa")));
		Assert.assertFalse("Test get own methods 2", d.getUMLClassOwnMethods("a").contains(new UMLClassMethod("bb")));
		Assert.assertTrue("Test get inherited methods 1", d.getUMLClassInheritedMethods("a").contains(new UMLClassMethod("bb")));
		Assert.assertFalse("Test get inherited methods 2", d.getUMLClassInheritedMethods("a").contains(new UMLClassMethod("aa")));

		/*Test consistencyCheck*/
		Assert.assertTrue("Test consistency 1", r.consistencyCheck());
		r.setTo("asdf");
		Assert.assertFalse("Test consistency 2", r.consistencyCheck());
		r.setTo(b.getName());
		r.addToFrom("asdf");
		Assert.assertFalse("Test consistency 3", r.consistencyCheck());

		d.delClass(a.getName());
		Assert.assertTrue("Test deleting class with relation 1", d.getRelations().contains(r));
		d.delClass(b.getName());
		Assert.assertFalse("Test deleting class with relation 2", d.getRelations().contains(r));

	}


	/**
	 * Test UMLClassInstance
	 */
	@Test
	public void testUMLClassInstance(){
		UMLClassDiagram cd = new UMLClassDiagram();
		/*Test successful creation of classInstance*/
		UMLClassInstance classInstance = new UMLClassInstance(cd, "c1", "Car");
		Assert.assertEquals("Test creating id", classInstance.createId(), "c1:Car");
		Assert.assertEquals("Test name", classInstance.getName(), "c1");
		Assert.assertEquals("Test class name", classInstance.getClassName(), "Car");
		/*Test changing value of attributes*/
		classInstance.setName("c5");
		classInstance.setClassName("Bus");
		Assert.assertEquals("Test changed name", classInstance.getName(), "c5");
		Assert.assertEquals("Test changed class name", classInstance.getClassName(), "Bus");
		/*Test equality of classInstances*/
		UMLClassInstance classInstance2 = new UMLClassInstance(cd, "c5", "Bus");
		Assert.assertEquals("Test comparing 2 class instances", classInstance, classInstance2);

	}

	/**
	 * Test UMLMessage
	 */
	@Test
	public void testUMLMessage(){
		UMLClassDiagram cd = new UMLClassDiagram();
		/*Test successful creation of UMLMessage*/
		UMLMessage msg = new UMLMessage(cd, new UMLSequenceDiagram("sd1", cd), "c1:Car", "b1:Bus", "Count max");
		Assert.assertEquals("Test UMLMessage value", msg.getMessage(), "Count max");
		Assert.assertEquals("Test UMLMessage 'from' instance", msg.getFrom(), "c1:Car");
		Assert.assertEquals("Test UMLMessage 'to' instance", msg.getTo(), "b1:Bus");
		/*Test changing value of message*/
		msg.setMessage("Count min");
		Assert.assertEquals("Test UMLMessage new value", msg.getMessage(), "Count min");
		/*Test changing value of 'from' instance*/
		msg.setFrom("c2:Car");
		Assert.assertEquals("Test UMLMessage new 'from' instance", msg.getFrom(), "c2:Car");
		/*Test changing value of 'to' instance*/
		msg.setTo("b2:Bus");
		Assert.assertEquals("Test UMLMessage new 'to' instance", msg.getTo(), "b2:Bus");


	}

	/**
	 * Test SequenceDiagram
	 */
	@Test
	public void testUMLSequenceDiagram() {
		UMLClassDiagram cd = new UMLClassDiagram();
		/*Test successful creation of UMLSequenceDiagram*/
		UMLSequenceDiagram sd = new UMLSequenceDiagram("sd1", cd);
		Assert.assertEquals("Test sequence diagram name", sd.getName(), "sd1");
		/*Test changing value of sequence diagram*/
		sd.setName("sd2");
		Assert.assertEquals("Test sequence diagram new name", sd.getName(), "sd2");
		/*Test UMLClassInstance*/
		Assert.assertTrue("Test add new instance", sd.addInstance(new UMLClassInstance(cd, "c1", "Car")));
		Assert.assertFalse("Test add existing instance", sd.addInstance(new UMLClassInstance(cd, "c1", "Car")));
		Assert.assertFalse("Test removing non-existing instance", sd.removeInstance(new UMLClassInstance(cd, "x1", "Hello")));
		/*Test UMLMessage*/
		UMLMessage message = new UMLMessage(cd, sd,"c1:Car", "b1:Bus", "Count max");
		sd.addMessage(message);
		Assert.assertTrue("Test removing existing message", sd.removeMessage(message));
	}
}

