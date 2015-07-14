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
		Coordinate vertCenter = this.coordinate;
		Coordinate contactPoint = new Coordinate( (this.coordinate.x + vector.origin.x) / 2 ,  (this.coordinate.y + vector.origin.y) / 2);
		Line lineVertCenterToContact = new Line(vertCenter, contactPoint);
		
		double perpendicularAngle = lineVertCenterToContact.angle * (-1);

		
		double adjustedValue = vector.toLine().angle - perpendicularAngle;
		if ((adjustedValue < 0) || (adjustedValue > Math.PI)) return false;
		
		double newMag = Math.sin(perpendicularAngle) * vector.getMagnitude();
		
		vector.dx = Math.sin(perpendicularAngle) * newMag;
		vector.dy = Math.cos(perpendicularAngle) * newMag;
		
		return true;
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
