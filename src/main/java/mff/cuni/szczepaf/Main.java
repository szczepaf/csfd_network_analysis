package mff.cuni.szczepaf;

public class Main {

    public static void main(String[] args) {

        String ID = "1096199";

        String jsonData = FilmDownloader.downloadByID(ID);
        Film f = FilmParser.parse(jsonData);
        System.out.println(f);


    }
}