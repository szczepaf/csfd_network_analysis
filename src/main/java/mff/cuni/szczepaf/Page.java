package mff.cuni.szczepaf;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class Page implements IMediaEntity {

    private ArrayList<String> movieURLs;

    public Page(ArrayList<String> movieURLs) {
        this.movieURLs = movieURLs;
    }


    public ArrayList<String> getMovieURLs() {
        return movieURLs;
    }
}
