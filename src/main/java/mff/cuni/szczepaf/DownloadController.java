package mff.cuni.szczepaf;

import java.io.IOException;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.HashSet;
public class DownloadController {

    /**
     * One of the main functions. It is used to retrieve all film links from given search parameters and store it in a specified file.
     * @param searchParams the suffix of the URL corresponding to search results in a CSFD detailed query.
     * @param timeout time between individual calls.
     * @param filename target filename to store the URLs in.
     */
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

    /**
     * Second main function. It fetches the Film URLs corresponding to a CSFD detailed query and downloads the film data. It stores them in a target file.
     * @param sourceFile file with the Film links.
     * @param targetFile file to store Film data in.
     * @param timeout time between individual calls.
     */
    public static void downloadFilmsFromLinks(String sourceFile, String targetFile, int timeout) {
        ArrayList<String> filmURLsToProcess;
        HashSet<String> existingFilmIDs = new HashSet<>();

        try {
            filmURLsToProcess = new ArrayList<>(Files.readAllLines(Paths.get(sourceFile)));
        } catch (IOException e) {
            System.err.println("Error reading source file: " + e.getMessage());
            return;
        }

        // Load existing film IDs from the target file
        try {
            ArrayList<String> existingLines = (ArrayList<String>) Files.readAllLines(Paths.get(targetFile));
            for (String line : existingLines) {
                if (line.contains(":")) {  // All lines should start with the ID of the movie - see PageParser class.
                    String id = line.substring(0, line.indexOf(':'));
                    existingFilmIDs.add(id);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading target file: " + e.getMessage());
        }

        // Extract film IDs from URLs
        FilmDownloader filmDownloader = new FilmDownloader(timeout);
        FilmParser filmParser = new FilmParser();
        ArrayList<String> filteredFilmURLs = new ArrayList<>();

        for (String url : filmURLsToProcess) {
            String filmID = filmParser.extractFilmIDFromURL(url);
            if (filmID != null && !existingFilmIDs.contains(filmID)) {
                filteredFilmURLs.add(url);
            }
        }

        System.out.println("Total Films: " + filmURLsToProcess.size());
        System.out.println("Already processed: " + (filmURLsToProcess.size() - filteredFilmURLs.size()));
        System.out.println("Remaining to process: " + filteredFilmURLs.size());

        int counter = 1;

        for (String url : filteredFilmURLs) {
            System.out.println("Downloading film: " + counter + " out of " + filteredFilmURLs.size());
            String filmString = filmDownloader.downloadByURL(url);
            Film film = filmParser.parse(filmString);
            if (film != null) {
                String filmID = filmParser.extractFilmIDFromURL(url);
                filmParser.dumpFilm(film, targetFile, filmID);
            }
            else {
                System.err.println("Something went wrong when downloading from URL: " + url);
                System.err.println("This url not be included in the target file.");
            }

            counter++;
        }

        System.out.println("Parsed, dumped. Finished.");
    }
}
