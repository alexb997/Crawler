package crawler.publi24;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.time.LocalDate;

public class PaginationHandler {
    public static String BASE_URL = "";

    public static String runCrawler() {
        LocalDate today = LocalDate.now();
        String url = BASE_URL;
        int pageNumber = 1;
        StringBuilder allResults = new StringBuilder();
        while (url != null) {
            try {
                Document doc = Jsoup.connect(url).get();
                String pageResults = Crawler.crawl(doc);
                allResults.append(pageResults);
                allResults.append("----------------------- Page Number: " + pageNumber++ + " -----------------------\n");

                // Use a pattern to extract the correct "next page" URL
                Element nextPageArrow = doc.selectFirst("ul.pagination.radius li.arrow a[href]");

                if (nextPageArrow != null && nextPageArrow.parent() != null && !nextPageArrow.parent().hasClass("unavailable")) {
                    String nextPageUrl = nextPageArrow.attr("href");

                    // Construct absolute URL if it's a relative path
                    if (!nextPageUrl.startsWith("http")) {
                        nextPageUrl = BASE_URL + nextPageUrl;
                    }

                    // Log the URL for debugging purposes
                    System.out.println("Next Page URL: " + nextPageUrl);

                    url = nextPageUrl;
                } else {
                    url = null;
                }

            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
        return allResults.toString();
    }
}
