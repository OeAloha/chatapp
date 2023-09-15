package oe.aloha;

public class Utils {
	private static char randOf(String chars) {
		return chars.charAt((int) Math.floor(Math.random() * chars.length()));
	}

	private static char randVowel() {
		return randOf("aeiou");
	}

	private static char randConsonant() {
		return randOf("bcdfghjklmnpqrstvwxyz");
	}

	public static String phoneticId() {
		String id = "";
		int start = (int) Math.round(Math.random());
		for (int i = 0; i < 10; i++) {
			id += (i % 2 == start) ? randConsonant() : randVowel();
		}
		return id;
	}
}
