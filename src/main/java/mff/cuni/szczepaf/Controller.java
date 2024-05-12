package mff.cuni.szczepaf;

import java.util.ArrayList;

public class Controller {

    public static void fetchFilmLinksFromSearchParams(String searchParams, int timeout, String filename){

        System.out.println("Starting to download pages for the given Search Parameters.");

        PageDownloader pageDownloader = new PageDownloader(timeout);
        String pagesInAString = pageDownloader.downloadByURL(searchParams);

        System.out.println("Pages downloaded.");

        System.out.println("Parsing and dumping pages.");
        System.out.println("...");

        PageParser pageParser = new PageParser();
        ArrayList<Page> parsedPages = pageParser.parsePages(pagesInAString);
        pageParser.dumpPages(filename, parsedPages);

        System.out.println("Pages parsed and dumped.");
    }

}
