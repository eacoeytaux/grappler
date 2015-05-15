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

	public Engine(GamePanel parentPanel) {
		this.parentPanel = parentPanel;

		loadColorSchemes();
		init();
	}

	public void init() {
		running = true;
		counter = 0;
		camera = new Camera(parentPanel.WIDTH, parentPanel.HEIGHT, parentPanel.WIDTH / 2, parentPanel.HEIGHT / 2);

		background = new TriangleBackground(parentPanel.WIDTH, parentPanel.HEIGHT, ColorSchemes.getScheme("BLUE"));

		map = new Map();
		p1 = new Player(Color.ORANGE, new Coordinate(100, 50));
		reset = false;
	}

	public void update() {
		if (reset) init();

		counter++;

		background.update(counter);
		
		map.update(counter);
		p1.update(map, counter);

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

	public void loadColorSchemes() {
		ColorSchemes.addColorScheme(new ColorScheme(new Color(0x000000), new Color(0x001933), new Color(0x003366), new Color(0x004C99), new Color(0x0066CC), new Color(0x0080FF), new Color(0x3399FF)), "BLUE");
		ColorSchemes.addColorScheme(new ColorScheme(new Color(0x000000), new Color(0x330019), new Color(0x660033), new Color(0x99004C), new Color(0x0066CC), new Color(0x0CC0066), new Color(0x0FF007F)), "RED");
		ColorSchemes.addColorScheme(new ColorScheme(new Color(330014), new Color(0x4C001F), new Color(0x660029), new Color(0x800033), new Color(0x99003D), new Color(0x1A000A), new Color(0x330014)), "PINK");
	}
}
