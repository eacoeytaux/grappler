package main;

import map.Coordinate;

public class Constants {
	
	public static double distance(double x1, double y1, double x2, double y2) {
		double xDiff = x1 - x2;
		double yDiff = y1 - y2;
		return Math.sqrt((xDiff * xDiff) + (yDiff * yDiff));
	}
	
	public static double distance(Coordinate coor1, Coordinate coor2) {
		return distance(coor1.getX(), coor1.getY(), coor2.getX(), coor2.getY());
	}
}
