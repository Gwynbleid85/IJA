package ija.app.uml;

public class ClassPosition {
	private String name;
	private double x;
	private double y;

	public ClassPosition(String name, double x, double y){
		this.name =name;
		this.x = x;
		this.y = y;
	}

	public String getName(){
		return name;
	}

	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
}
