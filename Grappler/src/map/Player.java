package map;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import main.Constants;

public class Player {
	ArrayList<Coordinate> positionMemory;
	int positionMemoryFreq = 1;
	int positionMemoryCap = 4;

	Color color;

	Coordinate lastCenter;
	Coordinate center;
	Vector vel;
	Vector gravity;

	MapEdge currentEdge;
	MapEdge currentVertex;

	double angle; //in radians

	int borderWidth = 8;
	int trueRadius;
	int colorRadius;
	int barRadius;

	float barPercentage;
	float barIncreaseSpeed = 0.005f;

	public Player(Color color, Coordinate center) {
		this.center = center;
		this.color = color;
		init();
	}

	public void init() {
		positionMemory = new ArrayList<Coordinate>();

		angle = -Math.PI / 2;

		vel = new Vector(center, 0, 0);
		gravity = new Vector(null, 0, 0.3);

		currentEdge = null;
		currentVertex = null;

		trueRadius = 40;
		colorRadius = trueRadius - borderWidth;
		barRadius = colorRadius + (borderWidth / 2);

		barPercentage = 0;
	}

	public void update(Map map, long counter) {
		if (barPercentage < 1) barPercentage += barIncreaseSpeed;
		if (barPercentage > 1) barPercentage = 1;

		if ((counter % positionMemoryFreq) == 0) {
			positionMemory.add(0, center.clone());
			while (positionMemory.size() > positionMemoryCap) positionMemory.remove(positionMemory.size() - 1);
		}


		if (currentEdge != null) {
			double newMag = currentEdge.friction * gravity.yDelta * Math.asin(currentEdge.line.angle);
			System.out.println(Math.toDegrees(currentEdge.line.angle));//Math.sin(currentEdge.line.angle) * newMag);
			vel.xDelta += Math.cos(currentEdge.line.angle) * newMag;
			vel.yDelta += Math.sin(currentEdge.line.angle) * newMag;
			//double velMagnitude = Constants.distance(0, 0, vel.xDelta, vel.yDelta);
			//System.out.println(velMagnitude);
			//vel = new Vector(vel.origin, velMagnitude * currentEdge.line.xAdjustment, velMagnitude * currentEdge.line.yAdjustment);
		} else {
			vel.addVector(gravity);
			Line velLine = new Line(vel.origin, vel.origin.clone(vel.xDelta, vel.yDelta));

			Collision collision = map.getCollision(velLine, currentEdge, null);
			if (collision != null) {
				collision.updatePlayer(this);
			}
		}

		center.x += vel.xDelta;
		
		angle += vel.xDelta / trueRadius;
		
		center.y += vel.yDelta;

	}

	public void draw(Graphics2D g2d, Camera camera) {
		float opacity = 0.25f;
		float opacityDecreaseValue = 0.05f;
		for (Coordinate coor : positionMemory) {
			opacity -= opacityDecreaseValue;
			drawCircle(g2d, camera, coor, opacity);
		}
		drawCircle(g2d, camera, center, 1);
	}

	public void drawCircle(Graphics2D g2d, Camera camera, Coordinate center, float opacity) {
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));

		g2d.setColor(Color.WHITE);
		g2d.fillOval(camera.xAdjust(center.x - trueRadius), camera.yAdjust(center.y - trueRadius), trueRadius * 2, trueRadius * 2);
		g2d.setColor(color);
		g2d.fillOval(camera.xAdjust(center.x - colorRadius), camera.yAdjust(center.y - colorRadius), colorRadius * 2, colorRadius * 2);

		//g2d.setColor(Color.BLACK);
		//g2d.drawOval(camera.xAdjust(center.x - barRadius), camera.yAdjust(center.y - barRadius), barRadius * 2, barRadius * 2);//, 90, -(int)(barPercentage * 360));
		//g2d.drawArc(camera.xAdjust(tempX - barRadius), camera.yAdjust(tempY - barRadius), barRadius * 2, barRadius * 2, 90, -(int)(barPercentage * 360));

		g2d.setColor(Color.WHITE);
		g2d.drawLine((int)center.x, (int)center.y, (int)(center.x + (colorRadius * Math.cos(angle))), (int)(center.y + (colorRadius * Math.sin(angle))));
	}
}
