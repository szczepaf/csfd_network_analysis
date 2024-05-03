package mff.cuni.szczepaf;

public interface IDownloader {
    String downloadByURL(String url);
    String downloadByID(String id);
}
