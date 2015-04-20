package map;

public class Coordinate {
	double x;
	double y;

	public Coordinate(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public Coordinate clone() {
		return clone(0, 0);
	}
	
	public Coordinate clone(double dx, double dy) {
		return new Coordinate(x + dx, y + dy);
	}
}
