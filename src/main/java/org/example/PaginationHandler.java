package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class PaginationHandler {
    private static final String BASE_URL = "https://www.olx.ro/oferte/q-BMV/";
    StringBuilder allResults = new StringBuilder();

    public static void main(String[] args) {
        String url = BASE_URL;
        int i = 1;
        StringBuilder allResults = new StringBuilder();
        while (url != null) {
            try {
                Document doc = Jsoup.connect(url).get();
                String pageResults = Crawler.crawl(doc);
                allResults.append(pageResults);

                Elements nextPage = doc.select("a[data-cy=pagination-forward]");
                url = nextPage.isEmpty() ? null : BASE_URL + nextPage.attr("href");
                allResults.append("Page Number: "+ i++ +"\n");

            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("results.txt"))) {
            writer.write(allResults.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
