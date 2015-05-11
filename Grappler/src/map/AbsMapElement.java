package map;

public abstract class AbsMapElement {
	
	public abstract Coordinate findCollision(Line line);
	
	//checks to see if player is exiting the element
	public abstract Coordinate checkExit(Line line);
	
	//returns the element that the player will transfer onto when leaving current element
	public abstract AbsMapElement getExitElement(Line line);
	
	public abstract boolean adjustVector(Vector vector);

}
