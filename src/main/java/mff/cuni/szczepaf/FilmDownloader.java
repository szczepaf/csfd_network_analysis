package mff.cuni.szczepaf;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

/**
 * FilmDownloader is a class that downloads film information from a given URL.
 */
public class FilmDownloader implements IDownloader {

    /**
     * Downloads film data in JSON format from the specified URL of a CSFD movie.
     * The JSON data is embedded in a tag "application/ld+json".
     * @param url The URL of the movie webpage.
     * @return A String containing the JSON data of the film. Null if the data is not found.
     */

    static String structureURLIDPrefix = "https://www.csfd.cz/film/";
    public static String downloadByURL(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            Element script = doc.selectFirst("script[type=application/ld+json]");

            if (script != null) {
                return script.data();
            } else {
                return null;
            }
        } catch (IOException e) {
            // Catch the IOException from Jsoup get method
            System.out.println("Unable to download the JSON with information about the movie.");
            System.out.println(e);
            return null;
        }
    }

    /**
     * Downloads and extracts film data in JSON format by film ID.
     * @param id The ID of the film to download.
     * @return A String containing the JSON data of the film.
     */
    public static String downloadByID(String id) {
        return downloadByURL(structureURLIDPrefix + id);
    }
}
