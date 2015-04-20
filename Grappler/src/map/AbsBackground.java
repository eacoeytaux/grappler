package map;

import java.awt.Graphics2D;
import java.util.Random;

public abstract class AbsBackground {
	Random random;
	
	//width of screen
	int width;
	//height of screen
	int height;
	
	ColorScheme[] schemes;

	public AbsBackground(int width, int height, ColorScheme... schemes) {
		random = new Random(System.currentTimeMillis());
		this.width = width;
		this.height = height;
		this.schemes = new ColorScheme[schemes.length];
		for (int i = 0; i < schemes.length; i++) {
			this.schemes[i] = schemes[i];
		}
	}
	
	//called to update the background - if background image is motionless should return immediately
	public abstract void update(long counter);
	
	//called to place the image on the canvas
	public abstract void draw(Graphics2D g2d);
}
