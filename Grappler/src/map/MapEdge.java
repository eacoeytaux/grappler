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
	public float friction = 0.99f;

	public MapEdge(Map map, MapVertex leftVertex, MapVertex rightVertex, boolean hookable) {
		super("Edge");
		this.parent = map;
		this.leftVertex = leftVertex;
		this.rightVertex = rightVertex;
		line = new Line(this.leftVertex.coordinate, this.rightVertex.coordinate);

		bumper = new Line(line.coor1.x + line.getPerpendicularDx(Player.trueRadius),
				line.coor1.y + line.getPerpendicularDy(Player.trueRadius),
				line.coor2.x + line.getPerpendicularDx(Player.trueRadius),
				line.coor2.y + line.getPerpendicularDy(Player.trueRadius));
		//backBumperLine = new Line(line.coor1.x + line.getPerpendicularDx(-Player.trueRadius),
				//line.coor1.y + line.getPerpendicularDy(-Player.trueRadius),
				//line.coor2.x + line.getPerpendicularDx(-Player.trueRadius),
				//line.coor2.y + line.getPerpendicularDy(-Player.trueRadius));

		int catchSize = 5;
		leftCatchLine = new Line(line.coor1.x + line.getPerpendicularDx(Player.trueRadius + catchSize),
				line.coor1.y + line.getPerpendicularDy(Player.trueRadius + catchSize),
				line.coor1.x + line.getPerpendicularDx(-Player.trueRadius - catchSize),
				line.coor1.y + line.getPerpendicularDy(-Player.trueRadius - catchSize));
		rightCatchLine = new Line(line.coor2.x + line.getPerpendicularDx(Player.trueRadius + catchSize),
				line.coor2.y + line.getPerpendicularDy(Player.trueRadius + catchSize),
				line.coor2.x + line.getPerpendicularDx(-Player.trueRadius - catchSize),
				line.coor2.y + line.getPerpendicularDy(-Player.trueRadius - catchSize));

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
		Line vectorLine = vector.toLine();

		//angle which vector is entering slope
		double adjustedAngle = (Math.PI / 2) - (vectorLine.angle - line.angle);
		if ((adjustedAngle < 0) || (adjustedAngle > Math.PI)) {
			//return false;
		}
		
		//gets magntitude of adjusted vector
		double newMag = Math.sin(adjustedAngle) * vector.getMagnitude();
		if (vector.dx < 0) newMag *= -1;
		
		//breaks adjusted vector into x and y components
		vector.dx = Math.cos(line.angle) * newMag * friction;
		vector.dy = Math.sin(line.angle) * newMag * friction;
		return true;
	}
}
