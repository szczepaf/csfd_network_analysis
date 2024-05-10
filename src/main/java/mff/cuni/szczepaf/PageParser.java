package mff.cuni.szczepaf;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class PageParser implements IParser {

    public static final String FILM_URL_PREFIX = "https://www.csfd.cz/";
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
    @Override
    public IMediaEntity parse(String HTMLSource) {
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
            System.out.println("Error while parsing HTML with found movies and extracting URLs.");
            System.out.println(e.getMessage());
            return null;
        }
        return new Page(urls);
    }

    public ArrayList<Page> parsePages(String concatenatedPages){
        ArrayList<String> pageStrings = splitPages(concatenatedPages);
        ArrayList<Page> pages = new ArrayList<Page>();
        for (String pageString : pageStrings){
            Page page = (Page) parse(pageString);
            pages.add(page);
        }

        return pages;
    }


    public void dumpPage(Page page, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            ArrayList<String> urls = page.getMovieURLs();
            for (String url : urls) {
                writer.write(url);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file " + filename + ": " + e) ;
        }
    }

    public void dumpPages(String filename, ArrayList<Page> pages){
        for (Page page : pages){
            dumpPage(page, filename);
        }
    }



}
