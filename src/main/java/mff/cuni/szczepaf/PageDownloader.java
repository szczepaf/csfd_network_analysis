package mff.cuni.szczepaf;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
public class PageDownloader implements IDownloader{


    private int timeout;
    private static final String URL_SEARCH_RESULTS_PREFIX = "https://www.csfd.cz/podrobne-vyhledavani/?page=PAGENUMBER&searchParams=";

    public PageDownloader(int timeout) {
        this.timeout = timeout;
    }

    public PageDownloader(){
        this.timeout = 30;
    }

    @Override
    public String downloadByID(String url){
        return null;
    }

    @Override
    public String downloadByURL(String searchParams) {
        int pageIndex = 1;
        boolean isEmpty = false;
        ArrayList<String> contactedPages = new ArrayList<>();

        try {
            while (!isEmpty) {
                String url = URL_SEARCH_RESULTS_PREFIX.replace("PAGENUMBER", String.valueOf(pageIndex)) + searchParams;

                System.out.println("Fetching page: " + pageIndex);
                Document doc = Jsoup.connect(url).get();
                String html = doc.html();
                isEmpty = pageIsEmpty(html);

                if (!isEmpty) {
                    pageIndex++;
                    contactedPages.add(html);

                    // Pause calls to avoid getting cut off
                    Thread.sleep(timeout * 1000L);
                }
            }
        } catch (IOException e) {
            System.err.println("Error fetching HTML: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted: " + e.getMessage());
        }

        // Join all pages collected into a single String with "PageSeparator" as the separator
        return String.join("PageSeparator", contactedPages);

    }


    private boolean pageIsEmpty(String pageHTML) {
        Document document = Jsoup.parse(pageHTML);
        return document.select(".film-title-name").isEmpty();
    }
}
