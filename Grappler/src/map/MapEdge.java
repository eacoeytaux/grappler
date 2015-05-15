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
		double vectorAngle = vector.toLine().angle;
		//if (vector.dx < 0) vectorAngle = (Math.PI / 2) - vectorAngle;

		/*if (vectorAngle < 0) vectorAngle = (Math.PI * 2) + vectorAngle;
		double lineAngle = line.angle;
		if (lineAngle < 0) lineAngle = (Math.PI * 2) + lineAngle;*/

		double adjustedValue = vectorAngle - line.angle;
		//System.out.println(Math.toDegrees(adjustedValue));
		if ((adjustedValue < 0) || (adjustedValue > Math.PI)) {
			//return false;
		}

		double tempAngle = (Math.PI / 2) - (vectorAngle - line.angle);//line.angle + ((Math.PI / 2) - vectorAngle);//Math.PI - (vectorAngle + line.angle);
		//System.out.println(Math.toDegrees(vectorAngle));
		
		
		//is correct
		double newMag = Math.sin(tempAngle) * vector.getMagnitude();
		if (vector.dx < 0) newMag *= -1;
		//line.angle + ((Math.PI / 2) - vectorAngle)
		
		vector.dx = Math.cos(line.angle) * newMag;
		vector.dy = Math.sin(line.angle) * newMag;

		return true;
	}
}
