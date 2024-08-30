package crawler.olx;

import crawler.gui.CrawlerApp;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class PaginationHandler {
    public static String BASE_URL = "";

    public static String runCrawler() {
        LocalDate today = LocalDate.now();
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
                allResults.append("----------------------- Page Number: " + i++ + " -----------------------\n");

            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
        return allResults.toString();
    }
}

