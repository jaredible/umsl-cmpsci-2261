package net.jaredible.code;

import java.util.Random;

public class Main {
	public static void main(String[] args) {
		Random r = new Random();
		char s = 'A';
		for (int i = 0; i < 10; i++) {
			System.out.println(String.format("%c%s%c %s%c%s%c", s + r.nextInt(26), r.nextInt(10), s + r.nextInt(26), r.nextInt(10), s + r.nextInt(26), r.nextInt(10), s + r.nextInt(26)));
		}
	}
}
