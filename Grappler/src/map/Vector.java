package map;

public class Vector {
	Coordinate origin;
	double xDelta;
	double yDelta;
	
	public Vector(Coordinate origin, Coordinate destination) {
		this(origin, origin.x - destination.x, origin.y - destination.y);
	}
	
	public Vector(Coordinate origin, double xDelta, double yDelta) {
		this.origin = origin;
		this.xDelta = xDelta;
		this.yDelta = yDelta;
	}
	
	public void addVector(Vector other) {
		xDelta += other.xDelta;
		yDelta += other.yDelta;
	}
}
