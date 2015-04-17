package map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class Engine {
	GamePanel parentPanel;
	
	Camera camera;
	Map map;
	Player p1;
	Player p2;
	
	public Engine(GamePanel parentPanel) {
		this.parentPanel = parentPanel;
		init();
	}
	
	public void init() {
		camera = new Camera(GamePanel.WIDTH, GamePanel.HEIGHT, GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2);
		map = new Map();
		p1 = new Player(new Coordinate(40, 40), 32, Color.ORANGE);
	}

	public void update() {
		map.update();
		p1.update();
		
		draw();
	}
	
	public void draw() {
		BufferedImage buffer = new BufferedImage(GamePanel.WIDTH, GamePanel.HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) buffer.getGraphics();

		map.draw(g, camera);
		p1.draw(g, camera);

		g.dispose();
		parentPanel.setImage(buffer);
	}
}
