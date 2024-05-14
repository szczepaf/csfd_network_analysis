package mff.cuni.szczepaf;

/**
 * Common interface for all downloaders - Page and Film downloaders (and potentially others in the future).
 */

public interface IDownloader {

    /**
     * Provides the HTML content from a URL resource.
     * @param url the URL
     * @return the HTML content, or its important parts.
     */
    public String downloadByURL(String url);

    /**
     * Sometimes, not always, it can be useful to not work with the whole URL, but just the ID of some Media Entity.
     * @param id ID of the Media Entity.
     * @return the HTML content, or its important parts.
     */
    public String downloadByID(String id);


}
