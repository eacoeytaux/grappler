package map;

public class MapVertex extends AbsMapElement {
	public final Map parent;
	public Coordinate coordinate;
	public MapEdge rightEdge;
	public MapEdge leftEdge;
	
	public MapVertex(Map map, Coordinate coordinate) {
		this.parent = map;
		this.coordinate = coordinate;
	}
	
	public MapEdge getEdge(boolean right) {
		return (right ? rightEdge : leftEdge);
	}
	
	public Coordinate findCollision(Line line) {
		return null; //TODO something
	}
	
	public Coordinate findTrapCollision(Line line) {
		return null; //TODO maybe something? maybe keep null...
	}
	
	public boolean adjustVector(Vector vector) {
		return true; //TODO something
	}
}
