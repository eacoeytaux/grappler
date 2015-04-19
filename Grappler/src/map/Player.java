package map;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Player {
	Color color;

	Coordinate center;

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
		angle = -Math.PI / 2;

		trueRadius = 40;
		colorRadius = trueRadius - borderWidth;
		barRadius = colorRadius + (borderWidth / 2);

		barPercentage = 0;
	}

	public void update(long counter) {
		if (barPercentage < 1) barPercentage += barIncreaseSpeed;
		if (barPercentage > 1) barPercentage = 1;
	}

	public void draw(Graphics2D g2d, Camera camera) {
		double tempX = center.x;
		double tempY = center.y;
		//for (int i = 0; i < 4; i++) {
			//g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (1f - ((float)(3 - i) * 0.25f))));
			g2d.setColor(Color.WHITE);
			g2d.fillOval(camera.xAdjust(tempX - trueRadius), camera.yAdjust(tempY - trueRadius), trueRadius * 2, trueRadius * 2);
			g2d.setColor(color);
			g2d.fillOval(camera.xAdjust(tempX - colorRadius), camera.yAdjust(tempY - colorRadius), colorRadius * 2, colorRadius * 2);

			//if (i == 3) {
				g2d.setColor(Color.BLACK);
				//g2d.drawOval(camera.xAdjust(center.x - barRadius), camera.yAdjust(center.y - barRadius), barRadius * 2, barRadius * 2);//, 90, -(int)(barPercentage * 360));
				g2d.drawArc(camera.xAdjust(tempX - barRadius), camera.yAdjust(tempY - barRadius), barRadius * 2, barRadius * 2, 90, -(int)(barPercentage * 360));

				g2d.setColor(Color.WHITE);
				g2d.drawLine((int)tempX, (int)tempY, (int)(tempX + (colorRadius * Math.cos(angle))), (int)(tempY + (colorRadius * Math.sin(angle))));
			//}

			
			tempX += 10;
			tempY += 10;
		//}
	}
}
