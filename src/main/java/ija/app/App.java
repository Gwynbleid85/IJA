package ija.app;

import ija.app.gui.G_UMLClass;
import ija.app.gui.G_UMLClassDiagram;
import ija.app.uml.classDiagram.UMLClass;
import ija.app.uml.classDiagram.UMLClassAttribute;
import ija.app.uml.classDiagram.UMLClassDiagram;
import ija.app.uml.classDiagram.UMLClassMethod;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Class for starting GUI, just a try, not part of Homework3!
 */
public class App extends Application
{
	public static void main(String[] args) {
		launch(args);
	}

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Hello World!");

		UMLClass cc = new UMLClass("asdf");
	    cc.addAttribute(new UMLClassAttribute("attrib1", "int", "+"));
	    cc.addAttribute(new UMLClassAttribute("attrib2", "int", "+"));
	    cc.addAttribute(new UMLClassAttribute("attrib3", "int", "+"));
	    cc.addMethod(new UMLClassMethod("method1", "-"));
	    cc.addMethod(new UMLClassMethod("method2", "-"));
	    cc.addMethod(new UMLClassMethod("method3", "-"));

	    UMLClassDiagram d = new UMLClassDiagram();
		d.addClass(cc);

	    G_UMLClassDiagram gd = new G_UMLClassDiagram(d);

        Group root = new Group();
	    root.getChildren().add(gd.getNode());
        primaryStage.setScene(new Scene(root, 500, 500));
        primaryStage.show();
    }
}
