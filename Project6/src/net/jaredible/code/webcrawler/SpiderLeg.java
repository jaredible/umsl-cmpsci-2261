package net.jaredible.code.webcrawler;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SpiderLeg {
	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
	private List<String> links = Collections.synchronizedList(new LinkedList<String>());
	private Document htmlDocument;

	public boolean test(String url) {
		try {
			if (url.isEmpty() || url.trim().equals("")) return false;

			Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
			connection.timeout(10000);
			Document htmlDocument = connection.get();
			this.htmlDocument = htmlDocument;

			if (connection.response().statusCode() == 200) Util.println("\n**Visiting** Received web page at " + url);

			String contentType = connection.response().contentType();
			if (contentType != null) {
				if (!contentType.contains("text/html")) {
					Util.printlnerr("\n**Failure** Retrieved something other than HTML");
					return false;
				}
			} else Util.printlnerr("ContentType is null!");

			Elements linksOnPage = htmlDocument.select("a[href]");
			for (Element link : linksOnPage)
				links.add(link.absUrl("href"));

			return true;
		} catch (IOException e) {
			Response response = null;

			try {
				response = Jsoup.connect(url).userAgent(USER_AGENT).timeout(100000).ignoreHttpErrors(true).execute();
			} catch (IOException e1) {
				Util.printlnerr("\nUnable to get " + url);
				return false;
			}

			if (response.statusCode() == 200) Util.println("\n**Visiting** Received web page at " + url);

			String contentType = response.contentType();
			if (contentType != null) {
				if (!contentType.contains("text/html")) {
					Util.printlnerr("\n**Failure** Retrieved something other than HTML");
					return false;
				}
			} else Util.printlnerr("ContentType is null!");

			Elements linksOnPage = htmlDocument.select("a[href]");
			for (Element link : linksOnPage)
				links.add(link.absUrl("href"));

			return true;
		}
	}

	public boolean crawl(String url) {
		try {
			if (url.isEmpty() || url.trim().equals("")) return false;

			Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
			Document htmlDocument = connection.get();
			this.htmlDocument = htmlDocument;

			if (connection.response().statusCode() == 200) Util.println("\n**Visiting** Received web page at " + url);

			String contentType = connection.response().contentType();
			if (contentType != null) {
				if (!contentType.contains("text/html")) {
					Util.printlnerr("**Failure** Retrieved something other than HTML");
					return false;
				}
			} else Util.printlnerr("ContentType is null!");

			Elements linksOnPage = htmlDocument.select("a[href]");
			Util.println("Found (" + linksOnPage.size() + ") links");
			for (Element link : linksOnPage)
				links.add(link.absUrl("href"));

			return true;
		} catch (IOException e) {
			Util.printlnerr("\nUnable to get " + url);
			return false;
		}
	}

	public boolean searchForWord(String searchWord) {
		if (htmlDocument == null) {
			Util.printlnerr("ERROR! Call crawl() before performing analysis on the document");
			return false;
		}

		Util.println("Searching for the word " + searchWord + "...");

		String bodyText = htmlDocument.body().text();
		return bodyText.toLowerCase().contains(searchWord.toLowerCase());
	}

	public List<String> getLinks() {
		return links;
	}
}
