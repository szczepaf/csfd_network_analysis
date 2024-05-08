package mff.cuni.szczepaf;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Page implements IMediaEntity {

    private ArrayList<String> movieURLs;

    public Page(String HTMLSource) {
        this.movieURLs = getURLsFromHTMLSource(HTMLSource);
    }

    /**
     * Parses HTML page of CSFD filtering results to extract URLs for found movies.
     * @param HTMLSource The HTML for one page of results.
     * @return An ArrayList of URLs as strings, empty if parsing fails.
     */
    public ArrayList<String> getURLsFromHTMLSource(String HTMLSource) {
        ArrayList<String> urls = new ArrayList<>();
        try {
            Document document = Jsoup.parse(HTMLSource);

            // Select all tags with the class 'film-title-name'
            Elements links = document.select("a.film-title-name");

            // Iterate over all elements found and extract the link
            for (Element link : links) {
                String url = link.attr("href");
                if (!url.isEmpty()) {
                    urls.add(url);
                }
            }
        } catch (Exception e) {
            System.out.println("Error while parsing HTML with found movies and extracting URLs.");
            System.out.println(e.getMessage());
            return null;
        }
        return urls;
    }

    public ArrayList<String> getMovieURLs() {
        return movieURLs;
    }
}
