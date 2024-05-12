package mff.cuni.szczepaf;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class FilmNetwork implements INetwork {

    private ArrayList<Film> nodes = new ArrayList<>();
    public void loadFilms(String filename, FilmCondition condition) {
        FilmParser filmParser = new FilmParser();
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            stream.forEach(line -> {
                try {
                    String jsonPart = line.split(":", 2)[1].trim(); // example line: 341532: {"name": "Láska, soudruhu", "duration": 101, "dateCreated": 2013, "directors": ["Taru Mäkelä"], "actors": ["Kati Outinen", "Miroslav Etzler", "Elena Leeve", "Kryštof Hádek", "Esko Salminen", "Laura Birn", "Vesa Vierikko", "Tommi Korpela", "Denny Ratajský", "Petr Stach"], "rating": 55.33869}
                    Film film = filmParser.parseFilmFromDump(jsonPart);
                    if (film != null && condition.meetsCondition(film)) {
                        nodes.add(film);
                    }
                } catch (Exception e) {
                    System.err.println("Failed to parse film: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            System.err.println("Failed to read file: " + e.getMessage());
        }
    }

    public void printNodes(){
        for (Film film : nodes){
            System.out.println(film);
        }
    }
    @Override
    public void createEdges(Object condition) {

    }
    public void export(String filename) {

    }
}

