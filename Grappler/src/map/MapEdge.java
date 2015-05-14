package map;

public class MapEdge extends AbsMapElement {
	public final Map parent;
	public MapVertex rightVertex;
	public MapVertex leftVertex;
	public Line line;
	public Line bumper;
	//public Line backBumperLine;
	public Line rightCatchLine;
	public Line leftCatchLine;
	public boolean hookable;
	public float friction = 1f;

	public MapEdge(Map map, MapVertex leftVertex, MapVertex rightVertex, boolean hookable) {
		this.parent = map;
		this.leftVertex = leftVertex;
		this.rightVertex = rightVertex;
		line = new Line(this.leftVertex.coordinate, this.rightVertex.coordinate);
		
		bumper = new Line(line.coor1.x + line.getPerpendicularDx(Player.trueRadius), line.coor1.y + line.getPerpendicularDy(Player.trueRadius), line.coor2.x + line.getPerpendicularDx(Player.trueRadius), line.coor2.y + line.getPerpendicularDy(Player.trueRadius));
		//backBumperLine = new Line(line.coor1.x + line.getPerpendicularDx(-Player.trueRadius), line.coor1.y + line.getPerpendicularDy(-Player.trueRadius), line.coor2.x + line.getPerpendicularDx(-Player.trueRadius), line.coor2.y + line.getPerpendicularDy(-Player.trueRadius));

		int catchSize = 5;
		leftCatchLine = new Line(line.coor1.x + line.getPerpendicularDx(Player.trueRadius + catchSize), line.coor1.y + line.getPerpendicularDy(Player.trueRadius + catchSize), line.coor1.x + line.getPerpendicularDx(-Player.trueRadius - catchSize), line.coor1.y + line.getPerpendicularDy(-Player.trueRadius - catchSize));
		rightCatchLine = new Line(line.coor2.x + line.getPerpendicularDx(Player.trueRadius + catchSize), line.coor2.y + line.getPerpendicularDy(Player.trueRadius + catchSize), line.coor2.x + line.getPerpendicularDx(-Player.trueRadius - catchSize), line.coor2.y + line.getPerpendicularDy(-Player.trueRadius - catchSize));
		
		this.hookable = hookable;
	}
	
	public Coordinate findCollision(Line line) {
		return bumper.intersection(line);
	}
	
	public Coordinate findTrapCollision(Line line) {
		Coordinate coor = rightCatchLine.intersection(line);
		if (coor == null) coor = leftCatchLine.intersection(line);
		return coor;
	}
	
	public boolean adjustVector(Vector vector) {
		double adjustedValue = vector.toLine().angle - line.angle;
		System.out.println(Math.toDegrees(vector.toLine().angle));
		if ((adjustedValue < 0) || (adjustedValue > Math.PI)) {
			//return false;
		}
		
		double newMag = Math.sin(line.angle + ((Math.PI / 2) - vector.toLine().angle)) * vector.getMagnitude();
		
		vector.dx = Math.cos(line.angle) * newMag;
		vector.dy = Math.sin(line.angle) * newMag;
		
		return true;
	}
}
