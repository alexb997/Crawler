package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class PaginationHandler {
    private static final String BASE_URL = "https://www.olx.ro/oferte/q-BMV/";

    public static void main(String[] args) {
        String url = BASE_URL;
        int i = 1;
        while (url != null) {
            try {
                Document doc = Jsoup.connect(url).get();
                Crawler.crawl(doc);

                Elements nextPage = doc.select("a[data-cy=pagination-forward]");
                url = nextPage.isEmpty() ? null : BASE_URL + nextPage.attr("href");
                System.out.println("Page number: "+ i++);

            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
