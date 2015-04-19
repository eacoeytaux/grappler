package map;

public class MapEdge {
	public final Map parent;
	public MapVertex rightVertex;
	public MapVertex leftVertex;
	public Line line;
	public boolean hookable;

	public MapEdge(Map map, MapVertex leftVertex, MapVertex rightVertex, boolean hookable) {
		this.parent = map;
		this.leftVertex = leftVertex;
		this.rightVertex = rightVertex;
		line = new Line(this.leftVertex.coordinate, this.rightVertex.coordinate);
		this.hookable = hookable;
	}
}
