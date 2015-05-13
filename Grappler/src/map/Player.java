package map;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.Constants;
import main.GamePanel;

public class Player {
	//trail
	ArrayList<Coordinate> positionMemory;
	int positionMemoryFreq = 2; 
	int positionMemoryCap = 16;

	Color color;

	Coordinate lastCenter;
	Coordinate center;
	Vector vel;

	double velocityMagnitude;

	Vector gravity;

	AbsMapElement currentElement;

	double angle; //rotation angle in radians

	boolean drawAimer = false;
	double aimerAngle;
	static final int aimerRadius = 72;

	static final int borderWidth = 8;
	static final int trueRadius = 40;
	static final int colorRadius = trueRadius - borderWidth;
	static final int barRadius = colorRadius + 4;

	float barPercentage;
	float barIncreaseSpeed = 0.005f;
	
	int energyRedValue = 0;
	Color energyColor = Color.BLACK;

	int[] xPoints;
	int[] yPoints;
	
	double edges = 16;
	String patternFileName = "pattern-5.jpg";
	BufferedImage pattern;

	public Player(Color color, Coordinate center) {
		this.center = center;
		this.color = color;
		init();
	}

	public void init() {
		positionMemory = new ArrayList<Coordinate>();

		angle = -Math.PI / 2;

		aimerAngle = 0;

		vel = new Vector(center, 0, 0);
		gravity = new Vector(null, 0, 0.2);

		currentElement = null;

		barPercentage = 0;

		try { //load images
			pattern = ImageIO.read(getClass().getResourceAsStream("/" + patternFileName));
		} catch (IOException e) {
			GamePanel.handleException(e);
		}
		
		setClipRegion();
	}

	public void update(Map map, long counter) {
		//if (true) return;

		//energy
		if (barPercentage < 1) barPercentage += barIncreaseSpeed;
		if (barPercentage > 1) barPercentage = 1;

		//trail
		if ((counter % positionMemoryFreq) == 0) {
			positionMemory.add(0, center.clone());
			while (positionMemory.size() > positionMemoryCap) positionMemory.remove(positionMemory.size() - 1);
		}

		//add forces to velocity
		vel.addVector(gravity);

		Vector tempVel = new Vector(vel.origin, vel.dx, vel.dy);
		//double nextVelMag = vel.getMagnitude(); //TODO apply this to the next velocity

		//TODO set magnitude of next velocity to appropriate magnitude
		while ((tempVel.dx != 0) || (tempVel.dy != 0)) {
			double velMag = tempVel.getMagnitude();

			//adjust velocity vector
			if (currentElement != null) {
				currentElement.adjustVector(tempVel);
			}

			AbsMapElement collidedElement = map.getCollision(tempVel, currentElement);

			if (collidedElement != null) { //collision detected
				Coordinate collisionCoor = collidedElement.findCollision(tempVel.toLine());
				float percentage = 1f - (float)Constants.distance(tempVel.origin, collisionCoor) / (float)velMag;
				center.x = collisionCoor.x;
				center.y = collisionCoor.y;
				currentElement = collidedElement;
				tempVel = new Vector(collisionCoor, tempVel.dx * percentage, tempVel.dy * percentage);
			} else { //no collision
				addVectorToCenter(tempVel);
				break;
			}
		}


		/*
		if (currentEdge != null) {

		} else if (currentVertex != null) {

		} else {

		}

		map.updatePlayerPos(this, gravity);

		vel.addVector(gravity);

		center.x += vel.xDelta;

		angle += vel.xDelta / trueRadius;
		angle %= Math.PI * 2;

		center.y += vel.yDelta;
		 */
	}

	public void addVectorToCenter(Vector vector) {
		center.x += vector.dx;
		center.y += vector.dy;
		vel.origin = center;

		angle += vector.dx / trueRadius;
		angle %= Math.PI * 2;
	}

	public void draw(Graphics2D g2d, Camera camera) {
		float percentage = 1f;
		float percentageDecreaseValue = 0.05f;
		float opacity = 0.02f * 8f;
		float opacityDecreaseValue = 0.02f;
		for (Coordinate coor : positionMemory) {
			percentage -= percentageDecreaseValue;
			opacity -= opacityDecreaseValue;

			if (percentage < 0) percentage = 0;
			if (opacity < 0) opacity = 0;

			drawCircleOutline(g2d, camera, coor, percentage, opacity);
			//drawCircle(g2d, camera, coor, percentage, opacity);
		}

		drawCircleOutline(g2d, camera, center, 1, 1);

		if (energyRedValue > 0) {
			energyRedValue -= 3;
			if (energyRedValue < 0) energyRedValue = 0;
			energyColor = new Color(energyRedValue, 0, 0);
		}
		
		g2d.setColor(energyColor);
		g2d.setStroke(new BasicStroke(3));
		g2d.fillArc(camera.xAdjust(center.x - barRadius), camera.yAdjust(center.y - barRadius), barRadius * 2, barRadius * 2, (int)Math.toDegrees(-angle), (int)(-barPercentage * 360));

		drawCircle(g2d, camera, center, 1, 1);

		//g2d.setColor(color);
		//g2d.drawLine((int)center.x, (int)center.y, (int)(center.x + (colorRadius * Math.cos(angle))), (int)(center.y + (colorRadius * Math.sin(angle))));

		//draw aimer
		if (drawAimer) {
			g2d.setColor(color);
			g2d.setStroke(new BasicStroke(4));
			g2d.drawArc(camera.xAdjust(center.x - aimerRadius), camera.yAdjust(center.y - aimerRadius), aimerRadius * 2, aimerRadius * 2, (int)-Math.toDegrees(aimerAngle) - 24, 9);
			g2d.drawArc(camera.xAdjust(center.x - aimerRadius), camera.yAdjust(center.y - aimerRadius), aimerRadius * 2, aimerRadius * 2, (int)-Math.toDegrees(aimerAngle) + 24, -9);

			Coordinate aimerLeftCoor = center.clone(Math.cos(aimerAngle - 0.2) * aimerRadius, Math.sin(aimerAngle - 0.17) * aimerRadius);
			g2d.drawOval(camera.xAdjust(aimerLeftCoor.x - 4), camera.yAdjust(aimerLeftCoor.y - 4), 8, 8);

			Coordinate aimerRightCoor = center.clone(Math.cos(aimerAngle + 0.2) * aimerRadius, Math.sin(aimerAngle + 0.17) * aimerRadius);
			g2d.drawOval(camera.xAdjust(aimerRightCoor.x - 4), camera.yAdjust(aimerRightCoor.y - 4), 8, 8);

			Coordinate aimerCoor = center.clone(Math.cos(aimerAngle) * aimerRadius, Math.sin(aimerAngle) * aimerRadius);
			g2d.drawOval(camera.xAdjust(aimerCoor.x - 8), camera.yAdjust(aimerCoor.y - 8), 16, 16);
		}

		drawKaleidoscope(g2d, camera);
	}

	public void drawCircleOutline(Graphics2D g2d, Camera camera, Coordinate center, float percentage, float opacity) {
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
		g2d.setColor(color);

		int tempRadius = (int)(trueRadius * percentage);
		g2d.fillOval(camera.xAdjust(center.x - tempRadius), camera.yAdjust(center.y - tempRadius), tempRadius * 2, tempRadius * 2);

	}

	public void drawCircle(Graphics2D g2d, Camera camera, Coordinate center, float percentage, float opacity) {
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
		g2d.setColor(Color.WHITE);

		int tempRadius = (int)(colorRadius * percentage);
		g2d.fillOval(camera.xAdjust(center.x - tempRadius - 1), camera.yAdjust(center.y - tempRadius - 1), tempRadius * 2 + 2, tempRadius * 2 + 2);

	}

	public void drawKaleidoscope(Graphics2D g2d, Camera camera) {
		BufferedImage finalImage = new BufferedImage(colorRadius * 2, colorRadius * 2, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gFinal = (Graphics2D)finalImage.getGraphics();
		
		gFinal.setColor(color);
		gFinal.fillOval(colorRadius - 4, colorRadius - 4, 8, 8);
		
		//creates segment
		Polygon triangle = new Polygon(xPoints, yPoints, 3);
		BufferedImage segment = new BufferedImage(pattern.getWidth(), pattern.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D gSeg = (Graphics2D)segment.getGraphics();
		//gSeg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gSeg.setClip(triangle);

		//moves clipping segment
		for (int i = 0; i < 3; i++) {
			xPoints[i] += 1;
			//yPoints[i] += 1;
		}
		if (xPoints[2] >= pattern.getWidth()) setClipRegion();

		gSeg.drawImage(pattern, 0, 0, null);
		gSeg.dispose();

		//final kaleidoscope image (side note - how is this word not in the eclipse dictionary??)
		BufferedImage kal = new BufferedImage(colorRadius * 2, colorRadius * 2, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gKal = (Graphics2D)kal.getGraphics();

		gKal.drawImage(segment, colorRadius - xPoints[0] + 1, colorRadius - yPoints[0] - 1, segment.getWidth(), segment.getHeight(), null);
		gKal.drawImage(segment, ((colorRadius - xPoints[0]) + xPoints[0] * 2) - 1, colorRadius - yPoints[0] - 1, -segment.getWidth(), segment.getHeight(), null);

		AffineTransform tx = AffineTransform.getRotateInstance(-Math.PI / (edges / 4), kal.getWidth() / 2, kal.getHeight() / 2);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		
		for (int i = 0; i < edges / 2; i++) {
			kal = op.filter(kal, null);
			gFinal.drawImage(kal, 0, 0, null);
		}

		
		tx = AffineTransform.getRotateInstance(angle, finalImage.getWidth() / 2, finalImage.getHeight() / 2);
		op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		finalImage = op.filter(finalImage, null);
		
		
		g2d.drawImage(finalImage, camera.xAdjust(center.x) - colorRadius, camera.yAdjust(center.y) - colorRadius, null);
	}
	
	public void setClipRegion() {
		yPoints = new int[]{0, colorRadius, (int)(Math.cos(Math.toRadians(90f / (edges / 4))) * colorRadius)};
		xPoints = new int[]{0, 0, (int)(Math.sin(Math.toRadians(90f / (edges / 4))) * colorRadius)};
	}
}
