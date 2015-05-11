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
	
	public boolean adjustVector(Vector vector) {
		return true; //TODO something
	}

	public Coordinate checkExit(Line line) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbsMapElement getExitElement(Line line) {
		// TODO Auto-generated method stub
		return null;
	}
}
