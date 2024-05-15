package mff.cuni.szczepaf.testing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import mff.cuni.szczepaf.*;

import java.util.ArrayList;


public class FilmNetworkTest {
    @Test
    public void testFilmLoadingWithCondition(){
        String conditionJSONString = "{\"duration\":[\">100\"], \"rating\":[\"<85.0\"], \"dateCreated\":[\">2005\"], \"actors\":[{\"notcontains\":\"Christian Bale\"}]}";
        String filename = "FilmsForNodeFilterTesting.txt";
        FilmNetwork filmNetwork = new FilmNetwork();
        NodeCondition fc = ConditionFactory.createNodeConditionFromJson(conditionJSONString);
        filmNetwork.loadNodes(filename, fc, true);

        // These films should be present:
        // {"name": "Casino Royale", "duration":144, "dateCreated": 2006, "directors": ["Martin Campbell"], "actors": ["Daniel Craig", "Eva Green", "Mads Mikkelsen", "Judi Dench", "Jeffrey Wright", "Giancarlo Giannini", "Caterina Murino", "Simon Abkarian", "Isaach De Bankolé", "Jesper Christensen"], "rating": 82.504036}
        // {"name": "Gympl", "duration":110, "dateCreated": 2007, "directors": ["Tomáš Vorel st."], "actors": ["Tomáš Vorel ml.", "Jiří Mádl", "Eva Holubová", "Tomáš Matonoha", "Martina Procházková", "Lenka Haluzová", "Zuzana Bydžovská", "Max Kušiak", "Tomáš Hanák", "Jan Kraus"], "rating": 61.799515}

        FilmParser fp = new FilmParser();
        Film casinoRoyale = fp.parseFilmFromDump("{\"name\": \"Casino Royale\", \"duration\":144, \"dateCreated\": 2006, \"directors\": [\"Martin Campbell\"], \"actors\": [\"Daniel Craig\", \"Eva Green\", \"Mads Mikkelsen\", \"Judi Dench\", \"Jeffrey Wright\", \"Giancarlo Giannini\", \"Caterina Murino\", \"Simon Abkarian\", \"Isaach De Bankolé\", \"Jesper Christensen\"], \"rating\": 82.504036}");
        Film gympl = fp.parseFilmFromDump("{\"name\": \"Gympl\", \"duration\":110, \"dateCreated\": 2007, \"directors\": [\"Tomáš Vorel st.\"], \"actors\": [\"Tomáš Vorel ml.\", \"Jiří Mádl\", \"Eva Holubová\", \"Tomáš Matonoha\", \"Martina Procházková\", \"Lenka Haluzová\", \"Zuzana Bydžovská\", \"Max Kušiak\", \"Tomáš Hanák\", \"Jan Kraus\"], \"rating\": 61.799515}");

        assertTrue(filmNetwork.getNodes().contains(casinoRoyale), "The FilmNetwork does not contain Casino Royale, even though it should.");
        assertTrue(filmNetwork.getNodes().contains(gympl), "The FilmNetwork does not contain Gymlp, even though it should.");

    }

    @Test
    public void testEdgeCreationWithEdgeCondition(){
        FilmParser fp = new FilmParser();
        Film casinoRoyale = fp.parseFilmFromDump("{\"name\": \"Casino Royale\", \"duration\":144, \"dateCreated\": 2006, \"directors\": [\"Martin Campbell\"], \"actors\": [\"Daniel Craig\", \"Eva Green\", \"Mads Mikkelsen\", \"Judi Dench\", \"Jeffrey Wright\", \"Giancarlo Giannini\", \"Caterina Murino\", \"Simon Abkarian\", \"Isaach De Bankolé\", \"Jesper Christensen\"], \"rating\": 82.504036}");
        Film gympl = fp.parseFilmFromDump("{\"name\": \"Gympl\", \"duration\":110, \"dateCreated\": 2007, \"directors\": [\"Tomáš Vorel st.\"], \"actors\": [\"Tomáš Vorel ml.\", \"Jiří Mádl\", \"Eva Holubová\", \"Tomáš Matonoha\", \"Martina Procházková\", \"Lenka Haluzová\", \"Zuzana Bydžovská\", \"Max Kušiak\", \"Tomáš Hanák\", \"Jan Kraus\"], \"rating\": 61.799515}");
        Film gympl2 = fp.parseFilmFromDump("{\"name\": \"Gympl 2\", \"duration\":150, \"dateCreated\": 2010, \"directors\": [\"Tomáš Vorel st.\"], \"actors\": [\"Tomáš Vorel ml.\",\"Leonardo Di Caprio\", \"Jiří Mádl\", \"Eva Holubová\", \"Tomáš Matonoha\", \"Martina Procházková\", \"Lenka Haluzová\", \"Zuzana Bydžovská\", \"Max Kušiak\", \"Tomáš Hanák\", \"Jan Kraus\"], \"rating\": 71.1215}");

        ArrayList<Film> films = new ArrayList<>();
        films.add(casinoRoyale);
        films.add(gympl);
        films.add(gympl2);
        FilmNetwork filmNetwork = new FilmNetwork(films);

        String jsonCondition = "{\"commonActors\" : 2}";
        EdgeCondition edgeCondition = ConditionFactory.createEdgeConditionFromJson(jsonCondition);

        filmNetwork.createEdges(edgeCondition);

        assertTrue(filmNetwork.getEdges().stream().anyMatch(edge ->
                (edge.getFilm1().equals(gympl) && edge.getFilm2().equals(gympl2)) ||
                        (edge.getFilm1().equals(gympl2) && edge.getFilm2().equals(gympl))
        ), "Edge should exist between Gymlp and Gymlp 2");

        assertFalse(filmNetwork.getEdges().stream().anyMatch(edge ->
                (edge.getFilm1().equals(gympl) && edge.getFilm2().equals(casinoRoyale)) ||
                        (edge.getFilm1().equals(casinoRoyale) && edge.getFilm2().equals(gympl)) ||
                        (edge.getFilm1().equals(gympl2) && edge.getFilm2().equals(casinoRoyale)) ||
                        (edge.getFilm1().equals(casinoRoyale) && edge.getFilm2().equals(gympl2))
        ), "No edges should exist between either of the Gympls and Casino Royale");

    }

}
