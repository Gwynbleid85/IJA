package ija.app;


import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;

public class Homework3 {
    public static void main(String[] args) {
    JUnitCore junit = new JUnitCore();
    junit.addListener(new TextListener(System.out));
    junit.run(AppTest.class);
   }
}
