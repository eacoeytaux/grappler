package map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class RippleBackground extends AbsBackground {
	
	ArrayList<Ripple> ripples;
	
	public RippleBackground(int width, int height) {
		super(width, height);
		
		ripples = new ArrayList<Ripple>();
		ripples.add(new Ripple(random.nextInt(width), random.nextInt(height)));
	}

	@Override
	public void update(long counter) {
		for (Ripple ripple : ripples) {
			ripple.update();
		}
		
		if ((counter % 10) == 0) ripples.add(0, new Ripple(random.nextInt(width), random.nextInt(height)));
		
		while (ripples.size() > 16) {
			ripples.remove(ripples.size() - 1);
		}
	}

	@Override
	public void draw(Graphics2D g2d) {
		for (Ripple ripple : ripples) {
			ripple.draw(g2d);
		}
	}
	
	private class Ripple {
		int x;
		int y;
		int radius = 10;
		float opacity = 0.8f;
		
		float red = 153f/255f;
		float green = 0;
		float blue = 76f/255f;
		
		public Ripple(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public void update() {
			radius++;
			opacity -= 0.005f;
		}
		
		public void draw(Graphics2D g2d) {
			if (opacity >= 0) {
				g2d.setStroke(new BasicStroke(radius / 3));
				g2d.setColor(new Color(red, green, blue, opacity));
				g2d.drawOval(x - radius, y - radius, radius * 2, radius * 2);
			}
		}
	}

}
