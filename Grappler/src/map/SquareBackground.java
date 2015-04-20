package map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import map.AbsBackground;

public class SquareBackground extends AbsBackground {
	//
	final Color[] colorStages = {new Color(0x000000), new Color(0x202020), new Color(0x404040), new Color(0x606060), new Color(0x808080), new Color(0xA0A0A0), new Color(0xC0C0C0)};//, new Color(0x62B2FF)};
	
	int fallTime = 3;

	final int squareSize = 24;

	int squareRowNum;
	int squareColNum;

	int xOffset;
	int yOffset;

	final int colorChangeTime = 250;

	ArrayList<Square> squares;
	BufferedImage image;

	public SquareBackground(int width, int height) {
		super(width, height);

		squareRowNum = (height/ squareSize );
		squareColNum = (width / squareSize) + 2;

		xOffset = (width % squareSize)/2;
		yOffset = -squareSize;

		image = new BufferedImage((int)((double)width * 1.5), (int)((double)height * 1.5), BufferedImage.TYPE_INT_ARGB);
		
		squares = new ArrayList<Square>();
		//populateTriangles();
		
		for (int i = 0; i < 10000; i++) {
			update(i);
		}
		
	}
	
	public void populateSquares() {
		for (int i = 0; i < squareRowNum; i++) {
			int xOffset = this.xOffset;// : this.xOffset - squareSize;
			int yOffset = (i - 1) * squareSize;
			for (int j = 0; j < squareColNum; j++) {
				squares.add(new Square(random.nextInt(colorStages.length), random.nextInt(colorChangeTime) + 1, xOffset, yOffset));

				xOffset += squareSize;
			}
		}
	}
	
	public void addNewRow() {
		int xOffset = this.xOffset;
		for (int i = 0; i < squareColNum; i++) {
			if (random == null) System.out.println("y?");
			squares.add(0, new Square(random.nextInt(colorStages.length - 3) + 3, random.nextInt(colorChangeTime - 1) + 1, xOffset, 0));
			
			if (squares.size() > 1116) squares.remove(squares.size() - 1);
			
			xOffset += squareSize;
		}
		
		BufferedImage tempImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2dTemp = (Graphics2D)tempImage.getGraphics();
		g2dTemp.drawImage(image, 0, 0, null);
		g2dTemp.dispose();
		
		Graphics2D g2d = (Graphics2D)image.getGraphics();
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
		g2d.drawImage(tempImage, 0, squareSize, null);
		g2d.dispose();
	}

	@Override
	public void update(long counter) {
		boolean push = false;
		
		if ((counter % fallTime) == 0) {
			yOffset++;
			while (yOffset >= 0) {
				yOffset -= squareSize;
				addNewRow();
				push = true;
			}
		}
		
		for (Square square : squares) {
			square.update(counter, push);
		}
	}

	@Override
	public void draw(Graphics2D g2d) {
		for (Square triangle : squares) {
			triangle.draw();
		}
		
		g2d.drawImage(image, -squareSize, yOffset - squareSize, null);
	}

	private void drawSquare(Square squa) {
		Graphics2D g2d = (Graphics2D)image.getGraphics();
        
		g2d.setColor(colorStages[squa.stage]);
		
		int[] yPoints = new int[4];
		
		yPoints[0] = squa.yPoints[0] + squa.yOffset;// + yOffset;
		yPoints[1] = squa.yPoints[1] + squa.yOffset;// + yOffset;
		yPoints[2] = squa.yPoints[2] + squa.yOffset;// + yOffset;
		yPoints[3] = squa.yPoints[3] + squa.yOffset;// + yOffset

		g2d.fillPolygon(squa.xPoints, yPoints, 4);
		g2d.dispose();
	}

	private class Square {
		int stage;
		int timeOffset;

		boolean needsDrawing;

		int[] xPoints;
		int[] yPoints;
		int yOffset;

		public Square(int stage, int timeOffset, int xOffset, int yOffset) {
			this.stage = stage;
			this.timeOffset = timeOffset;

			needsDrawing = true;

			xPoints = new int[4];
			yPoints = new int[4];


		
			xPoints[0] = xOffset;
			xPoints[1] = xOffset;
			xPoints[2] = squareSize + xOffset;
			xPoints[3] = squareSize + xOffset;	
			yPoints[0] = yOffset;
			yPoints[1] = squareSize + yOffset;
			yPoints[3] = yOffset;
			yPoints[2] = squareSize + yOffset;
			 
		}

		public void update(long counter, boolean push) {
			if (((counter + timeOffset) % colorChangeTime) == 0) {
				if (stage > 0) {
					stage--;
					needsDrawing = true;
				}
			}
			if (push) yOffset += squareSize;
		}

		public void draw() {
			if (needsDrawing) {
				drawSquare(this);
				needsDrawing = false;
			}
		}
	}
}
