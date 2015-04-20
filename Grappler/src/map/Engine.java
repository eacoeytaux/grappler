package map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class Engine {
	long counter;
	boolean reset;
	
	GamePanel parentPanel;
	
	Camera camera;
	AbsBackground background;
	Map map;
	Player p1;
	Player p2;
	
	boolean running;
	Thread backgroundThread;
	BufferedImage backgroundImage;
	
	public Engine(GamePanel parentPanel) {
		this.parentPanel = parentPanel;
		
		init();
	}
	
	public void init() {
		running = true;
		counter = 0;
		camera = new Camera(parentPanel.WIDTH, parentPanel.HEIGHT, parentPanel.WIDTH / 2, parentPanel.HEIGHT / 2);
		background = new SquareWaveBackground(parentPanel.WIDTH, parentPanel.HEIGHT);
		backgroundImage = new BufferedImage(parentPanel.WIDTH, parentPanel.HEIGHT, BufferedImage.TYPE_INT_ARGB);
		map = new Map();
		p1 = new Player(Color.ORANGE, new Coordinate(50, 50));
		reset = false;
	}

	public void update() {
		if (reset) init();
		
		counter++;
		
		background.update(counter);
		map.update(counter);
		p1.update(counter);
		
		draw();
	}
	
	public void draw() {
		BufferedImage buffer = new BufferedImage(parentPanel.WIDTH, parentPanel.HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = (Graphics2D) buffer.getGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        background.draw(g2d);

		map.draw(g2d, camera);
		p1.draw(g2d, camera);

		g2d.dispose();
		parentPanel.setImage(buffer);
	}
	
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_R) reset = true;
	}
}
