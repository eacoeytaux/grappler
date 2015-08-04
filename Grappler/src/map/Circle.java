package map;

import main.Constants;

public class Circle{
	Coordinate center;
	double radius;
	
	public Circle(Coordinate center, double radius){
		this.center = center;
		this.radius = radius;
	}
	
	public boolean inLine(double x, double y) {
		return (Constants.distance(center.x, center.y, x, y) <= radius);
	}
	
	public Coordinate intersection(Line otherLine) {
		double baX = otherLine.coor2.x - otherLine.coor1.x;
        double baY = otherLine.coor2.y - otherLine.coor1.y;
        double caX = center.x - otherLine.coor1.x;
        double caY = center.y - otherLine.coor1.y;

        double a = baX * baX + baY * baY;
        double bBy2 = baX * caX + baY * caY;
        double c = caX * caX + caY * caY - radius * radius;

        double pBy2 = bBy2 / a;
        double q = c / a;

        double disc = pBy2 * pBy2 - q;
       
        //if there are no intersections
        if (disc < 0) 
            return null;
  
        double tmpSqrt = Math.sqrt(disc);
        double abScalingFactor1 = -pBy2 + tmpSqrt;
        double abScalingFactor2 = -pBy2 - tmpSqrt;

        Coordinate p1 = new Coordinate(otherLine.coor1.x - baX * abScalingFactor1, otherLine.coor1.y
                - baY * abScalingFactor1);
        if (disc == 0) { // abScalingFactor1 == abScalingFactor2
            return p1;
        }
        Coordinate p2 = new Coordinate(otherLine.coor1.x - baX * abScalingFactor2, otherLine.coor1.y
                - baY * abScalingFactor2);
        
        //return null if intersection is outside of line endpoints
        if(otherLine.coor1.x > otherLine.coor2.x ){
			if(p1.x > otherLine.coor1.x || p1.x < otherLine.coor2.x )
				return null;
		}else{
			if(p1.x < otherLine.coor1.x || p1.x > otherLine.coor2.x)
				return null;
		}
		if(otherLine.coor1.y > otherLine.coor2.y){
			if(p1.y > otherLine.coor1.y || p1.y < otherLine.coor2.y )
				return null;
		}else{
			if(p1.y < otherLine.coor1.y || p1.y > otherLine.coor2.y )
				return null;
		}
		
		
		if(Double.isNaN(p1.x))
			return null;
		
		
        return p1;
	}
}
