package map;

import java.awt.Color;
import java.awt.Graphics2D;

public class Player {
	Coordinate center;
	double radius;
	Color color;
	
	public Player(Coordinate center, double radius, Color color) {
		this.center = center;
		this.radius = radius;
		this.color = color;
	}
	
	public void update() {
		
	}
	
	public void draw(Graphics2D g, Camera camera) {
		g.setColor(color);
		g.fillOval(camera.xAdjust(center.x - radius), camera.yAdjust(center.y - radius), (int)radius * 2, (int)radius * 2);
	}
}
