package map;

public class Vector {
	Coordinate origin;
	double dx;
	double dy;
	
	public Vector(Coordinate origin, Coordinate destination) {
		this(origin, origin.x - destination.x, origin.y - destination.y);
	}
	
	public Vector(Coordinate origin, double dx, double dy) {
		this.origin = origin;
		this.dx = dx;
		this.dy = dy;
	}
	
	public void addVector(Vector other) {
		dx += other.dx;
		dy += other.dy;
	}
}
