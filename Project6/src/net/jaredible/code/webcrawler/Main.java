package net.jaredible.code.webcrawler;

import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class Main {
	public static byte[] reservedMemory = new byte[0xa00000];
	private static final boolean USE_VALIDATOR = true;
	private static final UrlValidator urlValidator = new UrlValidator(new String[] { "http", "https" });

	private static void freeMemory() {
		reservedMemory = new byte[0];
		System.gc();
	}

	public static void exit(int status) {
		System.exit(status);
	}

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		Thread.currentThread().setName("Spider main thread");

		String url = "";

		if (args.length > 0) {
			url = args[0].trim();
		}

		Scanner scanner = new Scanner(System.in);
		boolean valid;
		Connection connection;
		int statusCode = 0;
		String contentType = "";
		if (!url.isEmpty()) {
			try {
				connection = Jsoup.connect(url).userAgent(SpiderLeg.USER_AGENT);
				connection.get();
				statusCode = connection.response().statusCode();
				contentType = connection.response().contentType();
			} catch (IOException e) {
				Util.printlnerr("Please type an existing URL!");
			}
		} else {
			Util.print("Please use a valid url which has content! URL: ");

			do {
				url = scanner.nextLine();
				valid = urlValidator.isValid(url) || !USE_VALIDATOR;

				if (!valid) Util.printerr("Please type a valid URL: ");
				else try {
					connection = Jsoup.connect(url).userAgent(SpiderLeg.USER_AGENT);
					connection.get();
					statusCode = connection.response().statusCode();
					contentType = connection.response().contentType();
				} catch (IOException e) {
					Util.printerr("Please type an existing URL: ");
				}
			} while (!valid || statusCode != 200 || !contentType.contains("text/html"));
		}

		Thread runner = null;
		final Spider spider = new Spider();
		boolean crashed = false;
		try {
			Util.printlnerr("\nPress ENTER in console to end crawler and list pages.");

			Thread.currentThread().sleep(5000);

			final String s = url;
			runner = new Thread(() -> {
				try {
					spider.search(s);
					// spider.test(Arrays.asList(s));
				} catch (Exception e) {
					Util.printlnerr("\nSomething happened while crawling!");
					System.gc();
					System.exit(1);
				}
			});
			runner.setName("Spider runner thread");
			runner.setDaemon(true);
			runner.start();

			while (scanner.nextLine() == null) {
			}

			spider.stop();
			runner.join();
		} catch (OutOfMemoryError e) {
			crashed = true;
			freeMemory();
			Util.printlnerr("\nNo free memory!");
		} catch (Exception e) {
			crashed = true;
			Util.printlnerr("\nWhat happened!?");
		} finally {
			if (!crashed) exit(0);
		}

		scanner.close();
		System.gc();
	}
}
