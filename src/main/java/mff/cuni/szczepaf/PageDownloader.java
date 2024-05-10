package mff.cuni.szczepaf;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class PageDownloader implements IDownloader{


    private int timeout;
    private static final String URL_SEARCH_RESULTS_PREFIX = "https://www.csfd.cz/podrobne-vyhledavani/?page=PAGENUMBER&searchParams=";

    public PageDownloader(String searchParams, int timeout) {
        this.timeout = timeout;
    }

    @Override
    public String downloadByID(String url){
        return null;
    }

    @Override
    public String downloadByURL(String searchParams){
        int pageIndex = 1;
        boolean isEmpty = false;
        StringBuilder contactedPages = new StringBuilder();

        try {
            while (!isEmpty) {
                String url = URL_SEARCH_RESULTS_PREFIX.replace("PAGENUMBER", String.valueOf(pageIndex)) + searchParams;
                Document doc = Jsoup.connect(url).get();
                String html = doc.html();
                isEmpty = pageIsEmpty(html);

                if (!isEmpty) {
                    pageIndex++;
                    contactedPages.append(html);
                    contactedPages.append("PageSeparator");

                    // Pause calls to avoid getting cut off
                    Thread.sleep(timeout * 1000L);
                }
            }
        } catch (IOException e) {
            System.err.println("Error fetching HTML: " + e.getMessage());
        } finally {
            return contactedPages.toString(); // Ensure that partial results are returned
        }    }


    private boolean pageIsEmpty(String pageHTML) {
        Document document = Jsoup.parse(pageHTML);
        return document.select(".film-title-name").isEmpty();
    }
}
