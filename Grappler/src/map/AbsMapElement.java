package map;

public abstract class AbsMapElement {
	private static int idNum = 0;
	String id;
	
	public static void resetId() {
		idNum = 0;
	}
	
	public AbsMapElement(String type) {
		id = type + "-" + idNum++;
	}
	
	public abstract Coordinate findCollision(Line line);

	public abstract Coordinate findTrapCollision(Line line);
	
	public abstract boolean adjustVector(Vector vector);

}
