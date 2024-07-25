package org.example;

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
        if (dateText.contains("Azi")) {
            return today.format(DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.forLanguageTag("ro")));
        }
        String[] parts = dateText.split(" - ");
        if (parts.length > 1) {
            String datePart = parts[1].trim();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.forLanguageTag("ro"));
            DateTimeFormatter parser = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.forLanguageTag("ro"));
            try {
                LocalDate date = LocalDate.parse(datePart, parser);
                return date.format(formatter);
            } catch (DateTimeParseException e) {
                return dateText;
            }
        }
        return dateText;
    }
    public static String crawl(Document doc) {
        StringBuilder result = new StringBuilder();
        Elements cards = doc.select("[data-cy=l-card]");

        for (Element card : cards) {
            Element titleElement = card.selectFirst("h6.css-1wxaaza");
            String title = titleElement != null ? titleElement.text() : "No title";

            boolean isPromoted = card.selectFirst("[data-testid=adCard-featured]") != null;

            Element priceElement = card.selectFirst("[data-testid=ad-price]");
            String price = priceElement != null ? priceElement.text() : "No price";

            Element dateElement = card.selectFirst("[data-testid=location-date]");
            String dateText = dateElement != null ? dateElement.text() : "No date";
            String date = processDate(dateText);

            result.append("Title: ").append(title).append("\n");
            result.append("Price: ").append(price).append("\n");
            result.append("Date: ").append(date).append("\n");
            result.append("Promoted: ").append(isPromoted ? "Yes" : "No").append("\n");
            result.append("-----------------------\n");
        }
        return result.toString();
    }
}