package map;

public class MapVertex {
	public final Map parent;
	public Coordinate coordinate;
	public boolean isGrabbable;
	public MapEdge rightEdge;
	public MapEdge leftEdge;
	
	public MapVertex(Map map, Coordinate coordinate) {
		this(map, coordinate, false);
	}
	
	public MapVertex(Map map, Coordinate coordinate, boolean isGrabbable) {
		this.parent = map;
		this.coordinate = coordinate;
		this.isGrabbable = isGrabbable;
	}
	
	public MapEdge getEdge(boolean right) {
		return (right ? rightEdge : leftEdge);
	}
}
