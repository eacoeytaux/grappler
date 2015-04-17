package map;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Camera {
	double xCenter;
	double yCenter;
	double halfWidth;
	double halfHeight;

	public Camera(double width, double height, double xCenter, double yCenter) {
		halfWidth = width / 2;
		halfHeight = height / 2;
		this.xCenter = xCenter;
		this.yCenter = yCenter;
	}

	public double getXCenter() {
		return xCenter;
	}

	public double getYCenter() {
		return yCenter;
	}

	public void setCenter(double xNew, double yNew) {
		//xSetCenter = xNew;
		//ySetCenter = yNew;
		xCenter = xNew;
		yCenter = yNew;
	}

	public int xAdjust(double x) {
		x -= (int)(xCenter - halfWidth);
		return (int)x;
	}

	public int xUnadjust(double x) {
		x += (int)(xCenter - halfWidth);
		return (int)x;
	}

	public int yAdjust(double y) {
		y -= (int)(yCenter - halfHeight);
		return (int)y;
	}

	public int yUnadjust(double y) {
		y += (int)(yCenter - halfHeight);
		return (int)y;
	}

	public void drawPicture(Graphics2D g, BufferedImage img, double x, double y) {
		g.drawImage(img, xAdjust(x), yAdjust(y), img.getWidth(), img.getHeight(), null);
	}
}
