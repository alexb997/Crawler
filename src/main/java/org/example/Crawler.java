package org.example;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {
    public static void crawl(Document doc) {
        Elements cards = doc.select("[data-cy=l-card]");

        for (Element card : cards) {
            Element titleElement = card.selectFirst("h6.css-1wxaaza");
            String title = titleElement != null ? titleElement.text() : "No title";

            boolean isPromoted = card.selectFirst("[data-testid=adCard-featured]") != null;

            System.out.println("Title: " + title);
            System.out.println("Promoted: " + (isPromoted ? "Yes" : "No"));
            System.out.println("-----------------------");
        }
    }
}