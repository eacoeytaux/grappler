package main;

import java.awt.Color;

import javax.swing.JFrame;

public class Grappler extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public static final boolean DEBUG = false;

	public static void main(String[] args) {
		JFrame window = new JFrame("Grappler");
		window.setContentPane(new GamePanel());
		window.setBackground(Color.BLACK);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}
}
