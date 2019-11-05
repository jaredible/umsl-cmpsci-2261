package net.jaredible.code.webcrawler;

public class Util {
	@SuppressWarnings("static-access")
	public synchronized static void print(String s) {
		synchronized (System.out) {
			System.out.print(s);
			System.out.flush();
		}

		try {
			Thread.currentThread().sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("static-access")
	public synchronized static void println(String s) {
		synchronized (System.out) {
			System.out.println(s);
			System.out.flush();
		}

		try {
			Thread.currentThread().sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("static-access")
	public synchronized static void printerr(String s) {
		synchronized (System.err) {
			System.err.print(s);
			System.err.flush();
		}

		try {
			Thread.currentThread().sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("static-access")
	public synchronized static void printlnerr(String s) {
		synchronized (System.err) {
			System.err.println(s);
			System.err.flush();
		}

		try {
			Thread.currentThread().sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
