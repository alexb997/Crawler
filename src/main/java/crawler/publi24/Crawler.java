package crawler.publi24;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class Crawler {

    private static String processDate(String dateText) {
        LocalDate today = LocalDate.now();
        String[] parts = dateText.split(" ");
        if (parts.length > 1) {
            String datePart = parts[0].trim() + " " + today.getMonthValue() + " " + today.getYear(); // Adjust if needed
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.forLanguageTag("ro"));
            try {
                LocalDate date = LocalDate.parse(datePart, formatter);
                return date.format(formatter);
            } catch (DateTimeParseException e) {
                return dateText;
            }
        }
        return dateText;
    }

    public static String crawl(Document doc) {
        StringBuilder result = new StringBuilder();
        Elements cards = doc.select("div.article-item"); // Selecting each ad card by its class

        for (Element card : cards) {
            // Extracting title
            Element titleElement = card.selectFirst("h2.article-title a");
            String title = titleElement != null ? titleElement.text() : "No title";

            // Checking if promoted (add your logic based on Publi24 structure, if applicable)
            boolean isPromoted = card.selectFirst(".popular-sign") != null;

            // Extracting price
            Element priceElement = card.selectFirst("p.article-price");
            String price = priceElement != null ? priceElement.text() : "No price";

            // Extracting date
            Element dateElement = card.selectFirst("p.article-date span");
            String dateText = dateElement != null ? dateElement.text() : "No date";
            String date = processDate(dateText);

            // Compiling results
            result.append("Title: ").append(title).append("\n");
            result.append("Price: ").append(price).append("\n");
            result.append("Date: ").append(date).append("\n");
            result.append("Promoted: ").append(isPromoted ? "Yes" : "No").append("\n");
            result.append("-----------------------\n");
        }
        return result.toString();
    }
}
