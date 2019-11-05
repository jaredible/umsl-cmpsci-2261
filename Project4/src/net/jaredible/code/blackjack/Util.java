package net.jaredible.code.blackjack;

public class Util {
	private static int NUM_MESSAGES = 0;

	public static void print(String message, int numSpacesBeginning) {
		String space = "";
		for (int i = 0; i < numSpacesBeginning; i++) {
			space += " ";
		}
		System.out.print(space + message);
		NUM_MESSAGES++;
	}

	public static void print(String message) {
		System.out.print(message);
		NUM_MESSAGES++;
	}

	public static void println(String message, int numSpacesBeginning) {
		String space = "";
		for (int i = 0; i < numSpacesBeginning; i++) {
			space += " ";
		}
		System.out.println(space + message);
		NUM_MESSAGES++;
	}

	public static void println(String message) {
		System.out.println(message);
		NUM_MESSAGES++;
	}

	public static void printerr(String message, int numSpacesBeginning) {
		String space = "";
		for (int i = 0; i < numSpacesBeginning; i++) {
			space += " ";
		}
		System.err.print(space + message);
		NUM_MESSAGES++;
	}

	public static void printerr(String message) {
		System.err.print(message);
		NUM_MESSAGES++;
	}

	public static void printlnerr(String message, int numSpacesBeginning) {
		String space = "";
		for (int i = 0; i < numSpacesBeginning; i++) {
			space += " ";
		}
		System.err.println(space + message);
		NUM_MESSAGES++;
	}

	public static void printlnerr(String message) {
		System.err.println(message);
		NUM_MESSAGES++;
	}

	public static int getNumMessages() {
		return NUM_MESSAGES;
	}
}
