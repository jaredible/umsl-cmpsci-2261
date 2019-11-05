package net.jaredible.code.webcrawler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Spider {
	private Set<String> pagesVisited = Collections.synchronizedSet(new HashSet<String>());
	private List<String> unvisitedPages = Collections.synchronizedList(new ArrayList<String>());
	private List<String> pagesToVisit = Collections.synchronizedList(new LinkedList<String>());
	private int numPagesCrawled = 0;
	private volatile boolean running = true;

	public synchronized void stop() {
		running = false;
	}

	public void displayURLs() {
		Util.println("\nList of web pages visited: ");
		for (String s : pagesVisited)
			if (unvisitedPages.contains(s)) Util.printlnerr(s);
			else Util.println(s);
	}

	private final ExecutorService executor = Executors.newCachedThreadPool();

	public void test(List<String> URLs) {
		Thread.yield();

		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		int l = URLs.size();
		for (int i = 0; i < l; i++) {
			final int j = i;
			executor.submit(() -> {
				SpiderLeg leg = new SpiderLeg();

				String currentURL = URLs.get(j);

				if (leg.test(currentURL)) {
					List<String> found = filterVisitedLinks(leg);
					pagesToVisit.addAll(found);
					test(found);
				}
			});
		}
	}

	public void search(String url) {
		search(url, "");
	}

	// TODO: Multithread
	public void search(String url, String searchWord) {
		String currentURL;
		SpiderLeg leg;

		do {
			if (!running) break;

			leg = new SpiderLeg();

			if (pagesToVisit.isEmpty()) {
				currentURL = url;
				pagesVisited.add(url);
			} else currentURL = nextUrl();

			if ("".equals(currentURL)) continue;

			if (leg.crawl(currentURL)) {
				numPagesCrawled++;
				pagesToVisit.addAll(filterVisitedLinks(leg));
			} else {
				unvisitedPages.add(currentURL);
			}

			// boolean success = leg.searchForWord(searchWord);
			// if (success) {
			// System.out.println(String.format("**Success** Word %s found at %s",
			// searchWord, currentURL));
			// search(currentURL, searchWord);
			// break;
			// }
		} while (!pagesToVisit.isEmpty());

		Util.println("\n**Done** Visited " + pagesVisited.size() + " web page(s)");
		Util.println("**Done** Crawled " + numPagesCrawled + " web page(s)");

		displayURLs();
		Main.exit(0);
	}

	private List<String> filterVisitedLinks(SpiderLeg leg) {
		List<String> links = new LinkedList<String>();

		for (String link : leg.getLinks())
			if (!pagesVisited.contains(link)) links.add(link);

		return links;
	}

	private String nextUrl() {
		String nextURL = "";

		do {
			if (!pagesToVisit.isEmpty()) nextURL = pagesToVisit.remove(0);
		} while (pagesVisited.contains(nextURL));

		if (!pagesVisited.contains(nextURL)) pagesVisited.add(nextURL);

		return nextURL;
	}
}
