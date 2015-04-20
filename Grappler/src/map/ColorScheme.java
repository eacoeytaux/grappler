package map;

import java.awt.Color;
import java.util.ArrayList;

public class ColorScheme {
	final ArrayList<Color> scheme;
	
	public ColorScheme(Color... colors) {
		scheme = new ArrayList<Color>();
		for (Color color : colors) {
			scheme.add(color);
		}
	}
	
	public Color getColor(int index) {
		return scheme.get(index);
	}
	
	public int getLength() {
		return scheme.size();
	}
}
