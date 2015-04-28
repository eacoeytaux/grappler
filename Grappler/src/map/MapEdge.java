package map;

public class MapEdge extends AbsMapElement {
	public final Map parent;
	public MapVertex rightVertex;
	public MapVertex leftVertex;
	public Line line;
	public Line frontBumperLine;
	public Line backBumperLine;
	public Line rightCatchLine;
	public Line leftCatchLine;
	public boolean hookable;
	public float friction = 1f;

	public MapEdge(Map map, MapVertex leftVertex, MapVertex rightVertex, boolean hookable) {
		this.parent = map;
		this.leftVertex = leftVertex;
		this.rightVertex = rightVertex;
		line = new Line(this.leftVertex.coordinate, this.rightVertex.coordinate);
		
		frontBumperLine = new Line(line.coor1.x + line.getPerpendicularDx(Player.trueRadius), line.coor1.y + line.getPerpendicularDy(Player.trueRadius), line.coor2.x + line.getPerpendicularDx(Player.trueRadius), line.coor2.y + line.getPerpendicularDy(Player.trueRadius));
		backBumperLine = new Line(line.coor1.x + line.getPerpendicularDx(-Player.trueRadius), line.coor1.y + line.getPerpendicularDy(-Player.trueRadius), line.coor2.x + line.getPerpendicularDx(-Player.trueRadius), line.coor2.y + line.getPerpendicularDy(-Player.trueRadius));

		int catchSize = 5;
		leftCatchLine = new Line(line.coor1.x + line.getPerpendicularDx(Player.trueRadius + catchSize), line.coor1.y + line.getPerpendicularDy(Player.trueRadius + catchSize), line.coor1.x + line.getPerpendicularDx(-Player.trueRadius - catchSize), line.coor1.y + line.getPerpendicularDy(-Player.trueRadius - catchSize));
		rightCatchLine = new Line(line.coor2.x + line.getPerpendicularDx(Player.trueRadius + catchSize), line.coor2.y + line.getPerpendicularDy(Player.trueRadius + catchSize), line.coor2.x + line.getPerpendicularDx(-Player.trueRadius - catchSize), line.coor2.y + line.getPerpendicularDy(-Player.trueRadius - catchSize));
		
		this.hookable = hookable;
	}
	
}
