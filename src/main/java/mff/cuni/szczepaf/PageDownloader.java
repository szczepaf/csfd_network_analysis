package mff.cuni.szczepaf;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Class that downloads the data of a detailed query on the CSFD portal.
 * It returns a long string with the HTML content, which is then used by the PageParser class.
 * This is the place to perform the detailed queries in CSFD:
 *<a href="https://www.csfd.cz/podrobne-vyhledavani/">https://www.csfd.cz/podrobne-vyhledavani/</a>
 */


public class PageDownloader implements IDownloader{


    private int timeout;
    // time between individual calls in seconds
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
    // not used as there is no page ID.

    /**
     * Downloads the HTML source based on the URL of the results of a search on CSFD. Goes through pages one by one until there are no more results.
     * @param searchParams the suffix of the search page (the part after searchParams=).
     * An example of such a string
     * (you should cut out everything before "searchParams="):
     * https://www.csfd.cz/podrobne-vyhledavani/?sort=rating_average&searchParams=rlW0rKOyVwcoZS0fVzqyoaWyVwc7VwRvBygqYPVlVwcoKFjvZlV6J10fVwDvBygqYPW0rKOyVwblsFjvo3WcM2yhVwc7VwRvBygqYPVlVwcoKFjvZlV6JmRfZGx3KFjvAPV6J10fVaE5pTHvBwA9YPW5MJSlK2Mlo20vBz51oTjfVayyLKWsqT8vBz51oTjfVaWuqTyhM19zpz9gVwchqJkfYPWlLKEcozqsqT8vBz51oTjfVaEuMlV6J10fVzSwqT9lVwcoKFjvMTylMJA0o3VvBygqYPWwo21jo3AypvV6J10fVaAwpzIyoaqlnKEypvV6J10fVzS1qTuipvV6J10fVzAcozIgLKEiM3WupTuypvV6J10fVaOlo2E1L3Eco24vBygqYPWyMTy0VwcoKFjvp291ozDvBygqYPWmL2Iho2qlLKObrFV6J10fVz1up2fvBygqYPWwo3A0qJ1yplV6J10fVzAiozEcqTyioaZvBygqsD
     * @return a long string with all the pages.
     */
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


    /**
     * Decides whether the page has any more films, or is it the page after the last page of the results?
     */
    private boolean pageIsEmpty(String pageHTML) {
        Document document = Jsoup.parse(pageHTML);
        return document.select(".film-title-name").isEmpty();
    }
}
