package map;

import main.Constants;

public class Circle{
	Coordinate center;
	double radius;
	
	public Circle(Coordinate center, double radius){
		this.center = center;
		this.radius = radius;
	}
	
	public boolean inLine(double x, double y) {
		return (Constants.distance(center.x, center.y, x, y) <= radius);
	}
	
	public Coordinate intersection(Line otherLine) {
		//TODO
		return null;
	}
}
