package map;

import java.awt.Graphics2D;
import java.util.Random;

public abstract class AbsBackground {
	Random random;
	int width;
	int height;

	public AbsBackground(int width, int height) {
		random = new Random(System.currentTimeMillis());
		this.width = width;
		this.height = height;
	}
	
	public abstract void update(long counter);
	
	public abstract void draw(Graphics2D g2d);
}
