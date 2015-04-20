package map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class TriangleBackground extends AbsBackground {
	//stages of colors each triangle goes through in descending order
	//final Color[] colorStages = {new Color(0x000000), new Color(0x001933), new Color(0x003366), new Color(0x004C99), new Color(0x0066CC), new Color(0x0080FF), new Color(0x3399FF)};//, new Color(0x62B2FF)};
	
	//number of cycles between moving background down 1 pixel
	int fallTime = 3;

	//half the triangle width/height in pixels
	final int triangleSize = 24;

	//number of triangles per column
	int triangleColNum;

	//whether or not the first triangle in a new row is pointing up
	boolean pointUpStart;
	//x-offset of first triangle - should remain constant
	int xOffset;
	//y-offset of which image should be drawn - should be between 0 and (triangleSize * 2)
	int yOffset;

	//number of cycles before a triangle changes color
	final int colorChangeTime = 250;

	//array of all triangles in background
	ArrayList<Triangle> triangles;
	//background image to be drawn to canvas
	BufferedImage image;

	public TriangleBackground(int width, int height, ColorScheme... schemes) {
		super(width, height, schemes);

		triangleColNum = ((int)((double)width * 1.5) / triangleSize) + 2;

		xOffset = (width % triangleSize) / 2;
		yOffset = -triangleSize * 2;

		image = new BufferedImage((int)((double)width * 1.5), (int)((double)height * 1.5), BufferedImage.TYPE_INT_ARGB);
		
		triangles = new ArrayList<Triangle>();
		
		for (int i = 0; i < 2000; i++) {
			update(i);
		}
		
	}
	
	public void addNewRow() {
		boolean pointUp = pointUpStart;
		pointUpStart = !pointUpStart; //since next row will be opposite of this row, pointUpStart is flipped
		int xOffset = this.xOffset;
		for (int i = 0; i < triangleColNum; i++) {
			ColorScheme scheme = schemes[random.nextInt(schemes.length)];
			triangles.add(0, new Triangle(scheme, random.nextInt(scheme.getLength() - 3) + 3, random.nextInt(colorChangeTime - 1) + 1, pointUp, xOffset, 0)); //adds new triangle to head of ArrayList
			
			while (triangles.size() > 1116) {
				System.out.println("!!!!");
				triangles.remove(triangles.size() - 1); //removes excess triangles from ArrayList
			}
			
			xOffset += triangleSize;
			pointUp = !pointUp; //flips whether or not next triangle is pointing up
		}
		
		//pushes image down triangleSize * 2 pixels
		BufferedImage tempImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2dTemp = (Graphics2D)tempImage.getGraphics();
		g2dTemp.drawImage(image, 0, 0, null);
		g2dTemp.dispose();
		
		Graphics2D g2d = (Graphics2D)image.getGraphics();
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
		g2d.drawImage(tempImage, 0, triangleSize * 2, null);
		g2d.dispose();
	}

	@Override
	public void update(long counter) {
		boolean push = false; //whether of not triangle yOffset needs to be updated
		
		if ((counter % fallTime) == 0) {
			yOffset++;
			if (yOffset >= 0) {
				yOffset -= triangleSize * 2; //resets yOffset to starting position
				addNewRow();
				push = true;
			}
		}
		
		for (Triangle triangle : triangles) {
			triangle.update(counter, push);
		}
	}

	@Override
	public void draw(Graphics2D g2d) {
		for (Triangle triangle : triangles) {
			triangle.draw();
		}
		
		g2d.drawImage(image, -triangleSize * 2, yOffset - (triangleSize * 2), null);
	}

	private void drawTriangle(Triangle tri) {
		Graphics2D g2d = (Graphics2D)image.getGraphics();
        
		g2d.setColor(tri.getColor());
		
		int[] yPoints = new int[3];
		
		yPoints[0] = tri.yPoints[0] + tri.yOffset;// + yOffset;
		yPoints[1] = tri.yPoints[1] + tri.yOffset;// + yOffset;
		yPoints[2] = tri.yPoints[2] + tri.yOffset;// + yOffset;

		g2d.fillPolygon(tri.xPoints, yPoints, 3);
		g2d.dispose();
	}

	private class Triangle {
		ColorScheme scheme;
		int stage;
		int timeOffset;

		boolean needsDrawing;

		int[] xPoints;
		int[] yPoints;
		int yOffset;

		public Triangle(ColorScheme scheme, int stage, int timeOffset, boolean pointUp, int xOffset, int yOffset) {
			this.scheme = scheme;
			this.stage = stage;
			this.timeOffset = timeOffset;

			needsDrawing = true;

			xPoints = new int[3];
			yPoints = new int[3];
			
			xPoints[0] = triangleSize + xOffset;
			xPoints[1] = (triangleSize * 2) + xOffset;
			xPoints[2] = (triangleSize * 3) + xOffset;

			if (pointUp) {
				yPoints[0] = (triangleSize * 2) + yOffset;
				yPoints[1] = yOffset;
				yPoints[2] = (triangleSize * 2) + yOffset;
			} else {
				yPoints[0] = yOffset;
				yPoints[1] = (triangleSize * 2) + yOffset;
				yPoints[2] = yOffset;
			}
		}

		public void update(long counter, boolean push) {
			if (((counter + timeOffset) % colorChangeTime) == 0) {
				if (stage > 0) {
					stage--;
					needsDrawing = true;
				}
			}
			if (push) yOffset += triangleSize * 2;
		}
		
		public Color getColor() {
			return scheme.getColor(stage);
		}

		public void draw() {
			if (needsDrawing) {
				drawTriangle(this);
				needsDrawing = false;
			}
		}
	}
}
