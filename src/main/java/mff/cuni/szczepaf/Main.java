package mff.cuni.szczepaf;

public class Main {

    public static void main(String[] args) {
        FilmDownloader downloader = new FilmDownloader();

        String ID = "1096199";

        String jsonData = downloader.downloadByID(ID);

}
