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
	
	//Checks for collisions with catch lines
	public Coordinate checkExit(Line line) {
		Coordinate exitCoor = null;
		
		Coordinate right = rightCatchLine.intersection(line);
		Coordinate left = leftCatchLine.intersection(line);
		
		if(right == null && left == null){
			exitCoor = null;
		}
		else if(right == null){
			exitCoor = left;
		}else{
			exitCoor = right;
		}
		
		return exitCoor;
	}
	
	public AbsMapElement getExitElement(Line line){
		//the element that the player will transfer onto 
		AbsMapElement exitElement = null;
		
		if( rightCatchLine.intersection(line) != null){
			exitElement = this.rightVertex;
		}else if(rightCatchLine.intersection(line) != null){
			exitElement = this.leftVertex;
		}
		
		return exitElement;
		
	}
	
	public boolean adjustVector(Vector vector) {
		double adjustedValue = vector.toLine().angle - line.angle;
		if ((adjustedValue < 0) || (adjustedValue > Math.PI)) return false;
		
		double newMag = Math.sin(line.angle) * vector.getMagnitude();
		
		vector.dx = Math.cos(line.angle) * newMag;
		vector.dy = Math.sin(line.angle) * newMag;
		
		return true;
	}
	
}
