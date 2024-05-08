package mff.cuni.szczepaf;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

public class PageParser implements IParser {

    private String searchParams;
    private int timeout;
    private static final String URL_SEARCH_RESULTS_PREFIX = "https://www.csfd.cz/podrobne-vyhledavani/?page=PAGENUMBER&searchParams=";

    public PageParser(String searchParams, int timeout) {
        this.searchParams = searchParams;
        this.timeout = timeout;
    }

    @Override
    public Page parse(String source) {
        return new Page(source);
    }

    public ArrayList<Page> parseSearchResults() {
        ArrayList<Page> pages = new ArrayList<>();
        int pageIndex = 1;
        boolean isEmpty = false;

        try {
            while (!isEmpty) {
                String url = URL_SEARCH_RESULTS_PREFIX.replace("PAGENUMBER", String.valueOf(pageIndex)) + searchParams;
                Document doc = Jsoup.connect(url).get();
                String html = doc.html();
                isEmpty = pageIsEmpty(html);

                if (!isEmpty) {
                    Page page = parse(html);
                    pages.add(page);
                    pageIndex++;
                    // Pause calls to avoid getting cut off
                    Thread.sleep(timeout * 1000);
                }
            }
        } catch (IOException e) {
            System.err.println("Error fetching HTML: " + e.getMessage());
        } finally {
            return pages; // Ensure that partial results are returned
        }
    }

    private boolean pageIsEmpty(String pageHTML) {
        Document document = Jsoup.parse(pageHTML);
        return document.select(".film-title-name").isEmpty();
    }
}
