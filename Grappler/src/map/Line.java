package map;

import map.Coordinate;

public class Line {
	double angle; //in radians
	Coordinate coor1; //left-most coordinate
	Coordinate coor2; //right-most coordinate
	double m;
	double b;
	boolean vertical;
	
	double xAdjustment;
	double yAdjustment;
	
	public Line(Coordinate coor1, Coordinate coor2) {
		this(coor1.x, coor1.y, coor2.x, coor2.y);
	}

	public Line(double x1, double y1, double x2, double y2) {
		if (x1 <= x2) {
			if ((x1 == x2) && (y1 > y2)) {
				double temp = y1;
				y1 = y2;
				y2 = temp;
			}
		} else {
			double temp;
			
			temp = x1;
			x1 = x2;
			x2 = temp;
			
			temp = y1;
			y1 = y2;
			y2 = temp;
		}
			
		coor1 = new Coordinate(x1, y1);
		coor2 = new Coordinate(x2, y2);

		if (x1 != x2) {
			vertical = false;
			m = (y2 - y1) / (x2 - x1);
			b = y1 - (m * x1);
			angle = Math.atan(m);
			xAdjustment = Math.cos(angle);
			yAdjustment = Math.sin(angle);
		} else {
			vertical = true;
			m = 0;
			b = 0;
			angle = 90;
			xAdjustment = 1;
			yAdjustment = 0;
		}
	}

	public boolean inLine(double x, double y) {
		if (vertical) {
			return ((x == coor1.x) && ((y >= coor1.y) && (y <= coor2.y)));
		} else {
			//if (((m * x) + b) != y) return false; TODO fix: rounding errors
			return (((x >= coor1.x) && (x <= coor2.x)) && ((m >= 0) ? ((y >= coor1.y) && (y <= coor2.y)) : ((y <= coor1.y) && (y >= coor2.y))));
		}
	}

	public Coordinate intersection(Line otherLine) {
		double xIntersect;
		double yIntersect;
		
		if (vertical) {
			if (otherLine.vertical) return null;
			xIntersect = coor1.x;
			yIntersect = (otherLine.m * coor1.x) + otherLine.b;
		} else if (otherLine.vertical) {
			xIntersect = otherLine.coor1.x;
			yIntersect = (m * otherLine.coor1.x) + b;
		} else {
			if (m == otherLine.m) {
				if (b == otherLine.b) return new Coordinate(0, m); //TODO should maybe print warning?
				else return null;
			}
			xIntersect = (otherLine.b - b) / (m - otherLine.m);
			yIntersect = (m * xIntersect) + b;
		}
		
		if (inLine(xIntersect, yIntersect) && otherLine.inLine(xIntersect, yIntersect)) {
			return new Coordinate(xIntersect, yIntersect);
		} else {
			return null;
		}
	}
	
	public double getPerpendicularDx(double distance) {
		return yAdjustment * distance;
	}
	
	public double getPerpendicularDy(double distance) {
		return xAdjustment * -distance;
	}
		
		/*
		double dx = yAdjustment * distance;
		double dy = xAdjustment * -distance;
		
		double angle = Math.toDegrees(this.angle);
		System.out.println(angle);
		double angleSin = Math.sin(angle);
		double angleCos = Math.cos(angle);
		System.out.println(angleSin);
		double dx = angleSin * distance;
		double dy = angleCos * distance;
		
		return new Line(coor1.x + dx, coor1.y + dy, coor2.x + dx, coor2.y + dy);
	}*/
}
