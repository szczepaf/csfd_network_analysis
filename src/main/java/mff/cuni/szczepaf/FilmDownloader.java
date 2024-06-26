package mff.cuni.szczepaf;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

/**
 * FilmDownloader is a class that downloads film information from a given URL.
 * Its information is then sent to the FilmParser class, which turns it into a Film.
 */
public class FilmDownloader implements IDownloader {

    public FilmDownloader(int timeout) {
        this.timeout = timeout;
    }
    public FilmDownloader(){
        this.timeout = 30;
    }

    private int timeout;    // Timeout in seconds between individual calls. Default = 30 seconds.

    static String structureURLIDPrefix = "https://www.csfd.cz/film/";


    /**
     * Downloads film data in JSON format from the specified URL of a CSFD movie.
     * The JSON data is embedded in a tag "application/ld+json" in the source.
     * @param url The URL of the movie webpage.
     * @return A String containing the JSON data of the film. Null if the data is not found.
     */

    public String downloadByURL(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            Element script = doc.selectFirst("script[type=application/ld+json]");

            if (script != null) {
                Thread.sleep(timeout * 1000L);
                return script.data();
            } else {
                return "Empty script for url: " + url;
            }
        } catch (IOException e) {
            // Catch the IOException from Jsoup get method
            System.err.println("Unable to download the JSON with information about the movie: " + e.getMessage());
            return "Unable to retrieve film info from url: " + url;
        } catch (InterruptedException e) {
            System.err.println("Thread interrupted: " + e.getMessage());
            return "Unable to retrieve film info from url: " + url;


        }
    }

    /**
     * Downloads and extracts film data in JSON format by film ID.
     * @param id The ID of the film to download.
     * @return A String containing the JSON data of the film.
     */
    public String downloadByID(String id) {
        return downloadByURL(structureURLIDPrefix + id);
    }


}
