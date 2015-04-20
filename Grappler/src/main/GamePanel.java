package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import map.Engine;

public class GamePanel extends JPanel implements Runnable, KeyListener, MouseListener {
	private static final long serialVersionUID = 1L;
	public final int WIDTH = 960;
	public final int HEIGHT = 640;

	public final int FPS = 60;
	public final long targetTime = 1000 / FPS;
	
	GamePanel self;
	Thread thread;
	Thread runningThread;
	Thread graphicsThread;
	boolean running;
	
	int runningMissedDeadlines;
	int graphicsMissedDeadlines;
	
	Engine engine;
	BufferedImage image;

	public GamePanel() {
		super();
		self = this;
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
	}

	@Override
	public void addNotify() {
		super.addNotify();
		if (thread == null) { //TODO
			image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
			thread = new Thread(this);
			addKeyListener(this);
			addMouseListener(this);
			thread.start();
		}
	}

	@Override
	public void run() {
		running = true;
		engine = new Engine(self);

		runningThread = new Thread(new Thread() {
			@Override
			public void run() {
				@SuppressWarnings({"unused"})
				int missedDeadlines = 0;
				long start, elapsed, wait;
				while (running) {
					start = System.nanoTime();
					engine.update();
					elapsed = System.nanoTime() - start;
					wait = targetTime - (elapsed / 1000000);
					if (wait >= 0) {
						try {
							Thread.sleep(wait);
						} catch(Exception e) {
							handleException(e);
						}
					} //else System.out.println("runningThread missed deadline! - Total:" + ++missedDeadlines);
				}
			}
		});
		
		graphicsThread = new Thread(new Thread() {
			@Override
			public void run() {
				@SuppressWarnings({"unused"})
				int missedDeadlines = 0;
				long start, elapsed, wait;
				while (running) {
					start = System.nanoTime();
					updateScreen();
					elapsed = System.nanoTime() - start;
					wait = targetTime - (elapsed / 1000000);
					if (wait >= 0) {
						try {
							Thread.sleep(wait);
						} catch(Exception e) {
							handleException(e);
						}
					} //else System.out.println("graphicsThread missed deadline! - Total:" + ++missedDeadlines);
				}
			}
		});
		
		runningThread.start();
		graphicsThread.start();
	}

	public void updateScreen() {
		Graphics g2d = getGraphics();
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();
	}

	public void setImage(BufferedImage newImage) {
		image = newImage;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		engine.keyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent e) { }

	@Override
	public void keyTyped(KeyEvent e) { }

	@Override
	public void mouseClicked(MouseEvent e) { }

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) { }

	@Override
	public void mousePressed(MouseEvent e) { }

	@Override
	public void mouseReleased(MouseEvent e) { }
	
	public static void handleException(Exception e) {
		e.printStackTrace();
	}
}
