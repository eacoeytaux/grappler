package main;

import java.util.HashMap;

public class Debug {
	public static final boolean DEBUG = false;
	
	public static HashMap<String, String> log = new HashMap<String, String>();
	
	public static void logMessage(String tag, String message) { //TODO in C++, make optional printMessage parameter
		log.get(tag).concat(message);
		if (DEBUG) System.out.println(message);
	}
	
	public static void logMessage(String tag, int message) {
		logMessage(tag, "" + message);
	}
	
	public static void logMessage(String tag, double message) {
		logMessage(tag, "" + message);
	}
	
	public static void logMessage(String tag, float message) {
		logMessage(tag, "" + message);
	}
}
