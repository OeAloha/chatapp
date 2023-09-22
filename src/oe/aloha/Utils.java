package oe.aloha;

import java.util.Scanner;

/**
 * Utility class. All methods should be static.
 */
public class Utils {
	private static Scanner scanner;

	private static char randOf(String chars) {
		return chars.charAt((int) Math.floor(Math.random() * chars.length()));
	}

	private static char randVowel() {
		return randOf("aeiou");
	}

	private static char randConsonant() {
		return randOf("bcdfghjklmnpqrstvwxyz");
	}

	/**
	 * Generates a random phonetic ID, like "zaxituzagi"
	 * 
	 * @return A random phonetic ID.
	 */
	public static String phoneticId() {
		String id = "";
		int start = (int) Math.round(Math.random());
		for (int i = 0; i < 10; i++) {
			id += (i % 2 == start) ? randConsonant() : randVowel();
			if (i == 0) {
				id = id.toUpperCase();
			}
		}
		return id;
	}

	public static void addScanner(Scanner scanner) {
		Utils.scanner = scanner;
	}

	public static void safeLog(String message) {
		System.out.print("\r" + message + "\n> ");
		System.out.flush();
		while (scanner.hasNext()) {
			scanner.next();
		}
	}
}
