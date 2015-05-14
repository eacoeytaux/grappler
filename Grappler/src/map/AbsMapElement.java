package map;

public abstract class AbsMapElement {
	
	public abstract Coordinate findCollision(Line line);
	
	public abstract Coordinate findTrapCollision(Line line);
	
	public abstract boolean adjustVector(Vector vector);
}
