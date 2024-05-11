package mff.cuni.szczepaf;

public class Main {

    public static void main(String[] args) {

        String ID = "2294";

        FilmDownloader fd = new FilmDownloader();

        String jsonData = fd.downloadByID(ID);
        System.out.println(jsonData);
        FilmParser fp =  new FilmParser();
        Film f = fp.parse(jsonData);
        System.out.println(f);


    }
}