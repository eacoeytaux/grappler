package map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class TriangleBackground extends AbsBackground {
	//
	final Color[] colorStages = {new Color(0x000000), new Color(0x001933), new Color(0x003366), new Color(0x004C99), new Color(0x0066CC), new Color(0x0080FF), new Color(0x3399FF)};//, new Color(0x62B2FF)};
	
	int fallTime = 3;

	final int triangleSize = 24;

	int triangleRowNum;
	int triangleColNum;

	boolean pointUpStart;
	int xOffset;
	int yOffset;

	final int colorChangeTime = 250;

	ArrayList<Triangle> triangles;
	BufferedImage image;

	public TriangleBackground(int width, int height) {
		super(width, height);

		triangleRowNum = ((height * 2) / (triangleSize * 3));
		triangleColNum = (width / triangleSize) + 2;

		xOffset = (width % triangleSize) / 2;
		yOffset = -triangleSize * 2;

		image = new BufferedImage((int)((double)width * 1.5), (int)((double)height * 1.5), BufferedImage.TYPE_INT_ARGB);
		
		triangles = new ArrayList<Triangle>();
		//populateTriangles();
		
		for (int i = 0; i < 10000; i++) {
			update(i);
		}
		
	}
	
	public void populateTriangles() {
		pointUpStart = true;
		
		for (int i = 0; i < triangleRowNum; i++) {
			boolean pointUp = pointUpStart;
			int xOffset = this.xOffset;// : this.xOffset - triangleSize;
			int yOffset = (i - 1) * (triangleSize * 2);
			for (int j = 0; j < triangleColNum; j++) {
				triangles.add(new Triangle(random.nextInt(colorStages.length), random.nextInt(colorChangeTime) + 1, pointUp, xOffset, yOffset));

				xOffset += triangleSize;
				pointUp = !pointUp;
			}
			pointUpStart = !pointUpStart;
		}
		
		pointUpStart = ((triangleRowNum % 2) == 0);
	}
	
	public void addNewRow() {
		boolean pointUp = pointUpStart;
		pointUpStart = !pointUpStart;
		int xOffset = this.xOffset;
		for (int i = 0; i < triangleColNum; i++) {
			if (random == null) System.out.println("y?");
			triangles.add(0, new Triangle(random.nextInt(colorStages.length - 3) + 3, random.nextInt(colorChangeTime - 1) + 1, pointUp, xOffset, 0));
			
			if (triangles.size() > 1116) triangles.remove(triangles.size() - 1);
			
			xOffset += triangleSize;
			pointUp = !pointUp;
		}
		
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
		boolean push = false;
		
		if ((counter % fallTime) == 0) {
			yOffset++;
			while (yOffset >= 0) {
				yOffset -= triangleSize * 2;
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
        
		g2d.setColor(colorStages[tri.stage]);
		
		int[] yPoints = new int[3];
		
		yPoints[0] = tri.yPoints[0] + tri.yOffset;// + yOffset;
		yPoints[1] = tri.yPoints[1] + tri.yOffset;// + yOffset;
		yPoints[2] = tri.yPoints[2] + tri.yOffset;// + yOffset;

		g2d.fillPolygon(tri.xPoints, yPoints, 3);
		g2d.dispose();
	}

	private class Triangle {
		int stage;
		int timeOffset;

		boolean needsDrawing;

		int[] xPoints;
		int[] yPoints;
		int yOffset;

		public Triangle(int stage, int timeOffset, boolean pointUp, int xOffset, int yOffset) {
			this.stage = stage;
			this.timeOffset = timeOffset;

			needsDrawing = true;

			xPoints = new int[3];
			yPoints = new int[3];


			if (pointUp) {
				xPoints[0] = triangleSize + xOffset;
				xPoints[1] = (triangleSize * 2) + xOffset;
				xPoints[2] = (triangleSize * 3) + xOffset;
				
				yPoints[0] = (triangleSize * 2) + yOffset;
				yPoints[1] = yOffset;
				yPoints[2] = (triangleSize * 2) + yOffset;
			} else {
				xPoints[0] = triangleSize + xOffset;
				xPoints[1] = (triangleSize * 2) + xOffset;
				xPoints[2] = (triangleSize * 3) + xOffset;
				
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

		public void draw() {
			if (needsDrawing) {
				drawTriangle(this);
				needsDrawing = false;
			}
		}
	}
}
