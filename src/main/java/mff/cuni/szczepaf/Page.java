package mff.cuni.szczepaf;

import java.util.ArrayList;

/**
 * Class that represents one page of search results from a detailed query of CSFD.
 * This is the page where you can perform such a query:
 * <a href="https://www.csfd.cz/podrobne-vyhledavani/">https://www.csfd.cz/podrobne-vyhledavani/</a>
 * One page typically holds 50 search results, in this case Films.
 * This class holds an ArrayList of the media URLs.
 * */
public class Page implements IMediaEntity {

    private ArrayList<String> mediaURLs;

    public Page(ArrayList<String> movieURLs) {
        this.mediaURLs = movieURLs;
    }


    public ArrayList<String> getMediaURLs() {
        return mediaURLs;
    }
}
