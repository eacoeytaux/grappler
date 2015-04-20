package map;

import java.util.HashMap;

public class ColorSchemes {
	static HashMap<String, ColorScheme> schemes = new HashMap<String, ColorScheme>();
	
	static boolean addColorScheme(ColorScheme scheme, String tag) {
		schemes.put(tag, scheme);
		return true;
	}
	
	static boolean containsScheme(String tag) {
		return schemes.containsKey(tag);
	}
	
	static ColorScheme getScheme(String tag) {
		return schemes.get(tag);
	}
	
}
