package mff.cuni.szczepaf;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Class that receives data from PageDownloader and turns it into pages with Media URLs.
 * It can also dump the pages into files.
 */

public class PageParser implements IParser {

    private static final String FILM_URL_PREFIX = "https://www.csfd.cz/";

    /**
     * Splits the long String from page downloader into individual pages.
     * @param concatenatedPages the long string with all the pages.
     * @return pages as strings.
     */
    public ArrayList<String> splitPages(String concatenatedPages){
        String[] pages = concatenatedPages.split("PageSeparator");
        ArrayList<String> splitPages = new ArrayList<>();
        for (String page : pages) {
            if (!page.trim().isEmpty()) {
                splitPages.add(page.trim());
            }
        }
        return splitPages;

    }

    /**
     * Parses the HTML corresponding to one page of search results to the Page IMediaEntity.
     * @param HTMLSource the source HTML.
     * @return a corresponding Page object.
     */
    @Override
    public Page parse(String HTMLSource) {
        ArrayList<String> urls = new ArrayList<>();
        try {
            Document document = Jsoup.parse(HTMLSource);

            // Select all tags with the class 'film-title-name'
            Elements links = document.select("a.film-title-name");

            // Iterate over all elements found and extract the link
            for (Element link : links) {
                String href = link.attr("href");
                String url = FILM_URL_PREFIX + href;
                urls.add(url);

            }
        } catch (Exception e) {
            System.err.println("Error while parsing HTML with found movies and extracting URLs.");
            System.err.println(e.getMessage());
            return null;
        }
        return new Page(urls);
    }

    /**
     * A wrapper method on the long string.
     * @param concatenatedPages the long string with all pages
     * @return a list of Pages.
     */
    public ArrayList<Page> parsePages(String concatenatedPages){
        if (concatenatedPages.isEmpty()) return null;

        ArrayList<String> pageStrings = splitPages(concatenatedPages);
        ArrayList<Page> pages = new ArrayList<Page>();
        for (String pageString : pageStrings){
            Page page = parse(pageString);
            pages.add(page);
        }

        return pages;
    }


    /**
     * Dumps the Page - which means dumping the Film URLs on the page.
     * @param page the Page to be dumped.
     * @param filename the target file.
     */
    public void dumpPage(Page page, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            ArrayList<String> urls = page.getMediaURLs();
            for (String url : urls) {
                writer.write(url);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file " + filename + ": " + e) ;
        }
    }

    /**
     * Wrapper method for dumping more pages
     */
    public void dumpPages(String filename, ArrayList<Page> pages){
        for (Page page : pages){
            dumpPage(page, filename);
        }
    }



}
