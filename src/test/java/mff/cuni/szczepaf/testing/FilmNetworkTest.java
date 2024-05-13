package mff.cuni.szczepaf.testing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import mff.cuni.szczepaf.*;

public class FilmNetworkTest {
    @Test
    public void testFilmLoadingWithCondition(){
        String conditionJSONString = "{\"duration\":[\">100\"], \"rating\":[\"<85.0\"], \"dateCreated\":[\">2005\"], \"actors\":[{\"notcontains\":\"Christian Bale\"}]}";
        String filename = "TestFiles/FilmsForNodeFilterTesting.txt";
        FilmNetwork filmNetwork = new FilmNetwork();
        NodeCondition fc = ConditionFactory.createNodeConditionFromJson(conditionJSONString);
        filmNetwork.loadNodes(filename, fc);

        // These films should be present:
        // {"name": "Casino Royale", "duration":144, "dateCreated": 2006, "directors": ["Martin Campbell"], "actors": ["Daniel Craig", "Eva Green", "Mads Mikkelsen", "Judi Dench", "Jeffrey Wright", "Giancarlo Giannini", "Caterina Murino", "Simon Abkarian", "Isaach De Bankolé", "Jesper Christensen"], "rating": 82.504036}
        // {"name": "Gympl", "duration":110, "dateCreated": 2007, "directors": ["Tomáš Vorel st."], "actors": ["Tomáš Vorel ml.", "Jiří Mádl", "Eva Holubová", "Tomáš Matonoha", "Martina Procházková", "Lenka Haluzová", "Zuzana Bydžovská", "Max Kušiak", "Tomáš Hanák", "Jan Kraus"], "rating": 61.799515}

        FilmParser fp = new FilmParser();
        Film casinoRoyale = fp.parseFilmFromDump("{\"name\": \"Casino Royale\", \"duration\":144, \"dateCreated\": 2006, \"directors\": [\"Martin Campbell\"], \"actors\": [\"Daniel Craig\", \"Eva Green\", \"Mads Mikkelsen\", \"Judi Dench\", \"Jeffrey Wright\", \"Giancarlo Giannini\", \"Caterina Murino\", \"Simon Abkarian\", \"Isaach De Bankolé\", \"Jesper Christensen\"], \"rating\": 82.504036}");
        Film gymlp = fp.parseFilmFromDump("{\"name\": \"Gympl\", \"duration\":110, \"dateCreated\": 2007, \"directors\": [\"Tomáš Vorel st.\"], \"actors\": [\"Tomáš Vorel ml.\", \"Jiří Mádl\", \"Eva Holubová\", \"Tomáš Matonoha\", \"Martina Procházková\", \"Lenka Haluzová\", \"Zuzana Bydžovská\", \"Max Kušiak\", \"Tomáš Hanák\", \"Jan Kraus\"], \"rating\": 61.799515}");



        assertTrue(filmNetwork.getNodes().contains(casinoRoyale), "The FilmNetwork does not contain Casino Royale, even though it should.");
        assertTrue(filmNetwork.getNodes().contains(gymlp), "The FilmNetwork does not contain Gymlp, even though it should.");

    }

}
