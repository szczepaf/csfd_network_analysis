package mff.cuni.szczepaf;

public class Main {

    public static void main(String[] args) {

        String ID = "1096199";

        String jsonData = FilmDownloader.downloadByID(ID);
        FilmParser fp =  new FilmParser();
        Film f = fp.parse(jsonData);
        System.out.println(f);


    }
}