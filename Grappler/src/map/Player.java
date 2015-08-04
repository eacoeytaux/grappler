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
import main.Debug;
import main.GamePanel;
import main.Grappler;

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

	Vector gravity = new Vector(null, 0, 0.1);
	
	Vector rightPush = new Vector(null, 0.1, 0);
	Vector leftPush = new Vector(null, -0.1, 0);
	boolean movingRight;
	boolean movingLeft;

	AbsMapElement currentElement;

	double angle; //rotation angle in radians

	boolean drawAimer = false;
	double aimerAngle;
	static final int aimerRadius = 72;

	static final int borderWidth = 6;
	static final int trueRadius = 40;
	static final int kaleidoscopeRadius = trueRadius - borderWidth;
	static final int kaleidoscopeBufferWidth = 2;
	static final int barRadius = kaleidoscopeRadius + 3;

	float barPercentage;
	float barIncreaseSpeed = 0.005f;

	int energyRedValue = 0;
	Color energyColor = Color.BLACK;

	int[] xPoints;
	int[] yPoints;

	double segments = 8;
	int kaleidoscopeSpeed = 2;
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

		currentElement = null;

		barPercentage = 0;

		try { //load images
			pattern = ImageIO.read(getClass().getResourceAsStream("/" + patternFileName));
		} catch (IOException e) {
			Grappler.handleException(e);
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
		if (movingRight) vel.addVector(rightPush);
		if (movingLeft) vel.addVector(leftPush);

		Vector tempVel = vel;//new Vector(vel.origin, vel.dx, vel.dy);
		//double nextVelMag = vel.getMagnitude(); //TODO apply this to the next velocity

		//TODO set magnitude of next velocity to appropriate magnitude
		while ((tempVel.dx != 0) || (tempVel.dy != 0)) {
			double velMag = tempVel.getMagnitude();

			//adjust velocity vector
			if (currentElement != null) {
				currentElement.adjustVector(tempVel);
			}
			
			Debug.logMessage("playerVel", tempVel.getMagnitude());

			//holds an element with which a collision is detected.  if no collision, null
			AbsMapElement collidedElement = map.getCollision(tempVel, currentElement);

			if (collidedElement != null) { //collision detected
				Coordinate collisionCoor = collidedElement.findCollision(tempVel.toLine());
				//temporarily allows for collisions with vertex when on a line.  will not work if colliding straight into vertex
				//if( collisionCoor == null ) collisionCoor = currentElement.findTrapCollision(tempVel.toLine());
				
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

		//kaleidoscope
		if ((counter % kaleidoscopeSpeed) == 0) {
			for (int i = 0; i < xPoints.length; i++) {
				xPoints[i] += 1;
				//yPoints[i] += 1;
			}
			if (xPoints[2] >= pattern.getWidth()) setClipRegion();
		}
	}

	public void addVectorToCenter(Vector vector) {
		center.x += vector.dx;
		center.y += vector.dy;
		vel.origin = center;

		angle += vector.dx / trueRadius;
		angle %= Math.PI * 2;
	}

	public void draw(Graphics2D g2d, Camera camera) {
		float percentage = 1f; //TODO make these global
		float percentageDecreaseValue = 0.05f;
		float opacity = 0.02f * 8f;
		float opacityDecreaseValue = 0.02f;
		for (Coordinate coor : positionMemory) {
			percentage -= percentageDecreaseValue;
			opacity -= opacityDecreaseValue;

			if (percentage < 0) percentage = 0;
			if (opacity < 0) opacity = 0;

			drawFullCircle(g2d, camera, coor, percentage, opacity);
			//drawCircle(g2d, camera, coor, percentage, opacity);
		}

		drawFullCircle(g2d, camera, center, 1, 1);

		if (energyRedValue > 0) {
			energyRedValue -= 3;
			if (energyRedValue < 0) energyRedValue = 0;
			energyColor = new Color(energyRedValue, 0, 0);
		}

		g2d.setColor(energyColor);
		g2d.setStroke(new BasicStroke(3));
		g2d.fillArc(camera.xAdjust(center.x - barRadius), camera.yAdjust(center.y - barRadius), barRadius * 2, barRadius * 2, (int)Math.toDegrees(-angle), (int)(-barPercentage * 360));

		drawWhiteCircle(g2d, camera, center, 1, 1);

		if (!Debug.DEBUG) {
			drawKaleidoscope(g2d, camera);
		} else { //draws velocity vector
			g2d.setStroke(new BasicStroke(1));
			g2d.setColor(Color.BLACK);
			g2d.drawLine((int)vel.origin.x, (int)vel.origin.y, (int)(vel.origin.x + vel.dx * 10), (int)(vel.origin.y + vel.dy * 10));
		}

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
	}

	public void drawFullCircle(Graphics2D g2d, Camera camera, Coordinate center, float percentage, float opacity) {
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
		g2d.setColor(color);

		int tempRadius = (int)(trueRadius * percentage);
		g2d.fillOval(camera.xAdjust(center.x - tempRadius), camera.yAdjust(center.y - tempRadius), tempRadius * 2, tempRadius * 2);

	}

	public void drawWhiteCircle(Graphics2D g2d, Camera camera, Coordinate center, float percentage, float opacity) {
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
		g2d.setColor(Color.WHITE);

		int tempRadius = (int)(kaleidoscopeRadius * percentage);
		g2d.fillOval(camera.xAdjust(center.x - tempRadius), camera.yAdjust(center.y - tempRadius), tempRadius * 2, tempRadius * 2);

	}

	public void drawKaleidoscope(Graphics2D g2d, Camera camera) {
		//kaleidoscope image (side note - how is this word not in the eclipse dictionary??)
		BufferedImage kaleidoscope = new BufferedImage(kaleidoscopeRadius * 2, kaleidoscopeRadius * 2, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gKaleidoscope = (Graphics2D)kaleidoscope.getGraphics();

		//sometimes there is a hole in the center of kaleidoscope, this fills that color with player color
		//gKaleidoscope.setColor(color);
		//gKaleidoscope.fillOval(kaleidoscopeRadius - 4, kaleidoscopeRadius - 4, 8, 8);

		//clips half segment from pattern
		Polygon triangle = new Polygon(xPoints, yPoints, 3);
		BufferedImage halfSegment = new BufferedImage(pattern.getWidth(), pattern.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D gHalfSeg = (Graphics2D)halfSegment.getGraphics();
		gHalfSeg.setClip(triangle);
		gHalfSeg.drawImage(pattern, 0, 0, null);
		gHalfSeg.dispose();

		//full segment image 
		BufferedImage segment = new BufferedImage(kaleidoscopeRadius * 2, kaleidoscopeRadius * 2, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gSegment = (Graphics2D)segment.getGraphics();

		gSegment.drawImage(halfSegment, kaleidoscopeRadius - xPoints[0], kaleidoscopeRadius - yPoints[0], halfSegment.getWidth(), halfSegment.getHeight(), null);
		gSegment.drawImage(halfSegment, ((kaleidoscopeRadius - xPoints[0]) + xPoints[0] * 2), kaleidoscopeRadius - yPoints[0], -halfSegment.getWidth(), halfSegment.getHeight(), null);
		gSegment.dispose();
		
		AffineTransform tx = AffineTransform.getRotateInstance(-Math.PI / (segments / 2), segment.getWidth() / 2, segment.getHeight() / 2);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

		//fills kaleidoscope with segments
		for (int i = 0; i < segments; i++) {
			segment = op.filter(segment, null);
			gKaleidoscope.drawImage(segment, 0, 0, null);
		}
		gKaleidoscope.dispose();

		//rotates kaleidoscope to angle
		tx = AffineTransform.getRotateInstance(angle, kaleidoscope.getWidth() / 2, kaleidoscope.getHeight() / 2);
		op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		kaleidoscope = op.filter(kaleidoscope, null);
		
		//draws kaleidoscope to screen
		g2d.drawImage(kaleidoscope, camera.xAdjust(center.x) - kaleidoscopeRadius, camera.yAdjust(center.y) - kaleidoscopeRadius, null);
	}

	public void setClipRegion() {
		int radius = kaleidoscopeRadius - kaleidoscopeBufferWidth;
		yPoints = new int[]{0, radius, (int)(Math.cos(Math.toRadians(90f / (segments / 2))) * radius)};
		xPoints = new int[]{0, 0, (int)(Math.sin(Math.toRadians(90f / (segments / 2))) * radius)};
	}
	
	public void setMovingRight(boolean b) {
		movingRight = b;
	}
	
	public void setMovingLeft(boolean b) {
		movingLeft = b;
	}
}


