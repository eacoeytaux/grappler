package map;

public class MapEdge {
	public final Map parent;
	public MapVertex rightVertex;
	public MapVertex leftVertex;
	public Line line;
	public boolean isFloor;
	public int strength;

	public MapEdge(Map map, MapVertex leftVertex, MapVertex rightVertex, boolean isFloor, int strength) {
		this.parent = map;
		this.leftVertex = leftVertex;
		this.rightVertex = rightVertex;
		line = new Line(this.leftVertex.coordinate, this.rightVertex.coordinate);
		this.isFloor = isFloor;
		this.strength = strength;
	}
}
