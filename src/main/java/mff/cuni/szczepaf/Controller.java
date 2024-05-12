package mff.cuni.szczepaf;

import java.io.IOException;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    public static void downloadFilmsFromLinks(String sourceFile, String targetFile, int timeout){
        ArrayList<String> filmURLs;

        try {
            filmURLs = new ArrayList<>(Files.readAllLines(Paths.get(sourceFile)));
        } catch (IOException e) {
            System.err.println("Error reading source file: " + e.getMessage());
            return;
        }
        System.out.println("Loaded film URLs, starting to download film data.");
        FilmDownloader filmDownloader = new FilmDownloader(timeout);
        FilmParser filmParser = new FilmParser();

        int counter = 1;

        for (String url : filmURLs){
            System.out.println("Downloading film: " + counter + " out of " + filmURLs.size());
            String filmString = filmDownloader.downloadByURL(url);
            Film film = filmParser.parse(filmString);
            filmParser.dumpFilm(film, targetFile);

            counter++;
        }

        System.out.println("Parsed, dumped. Finished.");



    }
}
