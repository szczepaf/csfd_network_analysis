package mff.cuni.szczepaf;

public class Main {

    public static void main(String[] args) {
        FilmDownloader downloader = new FilmDownloader();

        String ID = "1096199";

        String jsonData = downloader.downloadByID(ID);

        if (jsonData != null) {
            System.out.println("JSON Data:");
            System.out.println(jsonData);
        } else {
            System.out.println("No JSON data found at the specified URL.");
        }
    }
}
