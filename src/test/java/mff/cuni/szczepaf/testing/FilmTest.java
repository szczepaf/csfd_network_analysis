package mff.cuni.szczepaf.testing;


import mff.cuni.szczepaf.FilmParser;
import mff.cuni.szczepaf.Film;
import mff.cuni.szczepaf.FilmDownloader;
import org.junit.jupiter.api.Test;

import static java.lang.Math.round;
import static org.junit.jupiter.api.Assertions.*;

public class FilmTest {

    @Test
    public void testFilmParsing() {
        Film film = getFilm();

        // Check correctness of parsed params
        assertEquals("PT87M", film.getDuration());
        assertEquals("Ženy a život", film.getName());
        assertEquals("2022", film.getDateCreated());
        assertNotNull(film.getDirectors());
        assertEquals(1, film.getDirectors().size());
        assertEquals("Petr Zahrádka", film.getDirectors().get(0).getName());
        assertNotNull(film.getActors());
        assertEquals("Eva Salvatore Burešová", film.getActors().get(0).getName());
        assertEquals("Jakub Kohák", film.getActors().get(1).getName());

        assertNotNull(film.getRating());
        assertEquals(35, round(film.getRating().getRatingValue()));
        assertEquals(1328, film.getRating().getRatingCount());
    }

    private static Film getFilm() {
        String jsonInput = "{\"@context\":\"https://schema.org/\",\"@type\":\"Movie\",\"duration\":\"PT87M\",\"name\":\"Ženy a život\",\"dateCreated\":\"2022\",\"director\":[{\"@type\":\"Person\",\"name\":\"Petr Zahrádka\"}],\"actor\":[{\"@type\":\"Person\",\"name\":\"Eva Salvatore Burešová\"},{\"@type\":\"Person\",\"name\":\"Jakub Kohák\"},{\"@type\":\"Person\",\"name\":\"Jiří Lábus\"},{\"@type\":\"Person\",\"name\":\"Veronika Freimanová\"},{\"@type\":\"Person\",\"name\":\"Petr Buchta\"},{\"@type\":\"Person\",\"name\":\"Aneta Krejčíková\"},{\"@type\":\"Person\",\"name\":\"Jaromír Nosek\"},{\"@type\":\"Person\",\"name\":\"Barbora Mottlová\"},{\"@type\":\"Person\",\"name\":\"Olga Lounová\"},{\"@type\":\"Person\",\"name\":\"Eva Decastelo\"}],\"aggregateRating\":{\"@type\":\"AggregateRating\",\"worstRating\":0,\"bestRating\":100,\"ratingValue\":34.683734939759034,\"ratingCount\":1328,\"reviewCount\":78},\"image\":\"https://image.pmgstatic.com/cache/resized/w420/files/images/film/posters/166/191/166191636_3611a2.jpg\"}";
        FilmParser parser = new FilmParser();

        return parser.parse(jsonInput);
    }

    @Test
    public void testFilmDownloadByID() {
        FilmDownloader downloader = new FilmDownloader();
        String filmJson = downloader.downloadByID("2294");
        String Shawshenk = "{\"@context\":\"https://schema.org/\",\"@type\":\"Movie\",\"duration\":\"PT142M\",\"name\":\"Vykoupení z věznice Shawshank\",\"dateCreated\":\"1994\",\"director\":[{\"@type\":\"Person\",\"name\":\"Frank Darabont\"}],\"actor\":[{\"@type\":\"Person\",\"name\":\"Tim Robbins\"},{\"@type\":\"Person\",\"name\":\"Morgan Freeman\"},{\"@type\":\"Person\",\"name\":\"Bob Gunton\"},{\"@type\":\"Person\",\"name\":\"William Sadler\"},{\"@type\":\"Person\",\"name\":\"Clancy Brown\"},{\"@type\":\"Person\",\"name\":\"Gil Bellows\"},{\"@type\":\"Person\",\"name\":\"Mark Rolston\"},{\"@type\":\"Person\",\"name\":\"James Whitmore\"},{\"@type\":\"Person\",\"name\":\"Jeffrey DeMunn\"},{\"@type\":\"Person\",\"name\":\"Larry Brandenburg\"}],\"author\":[{\"@type\":\"Person\",\"name\":\"Stephen King\"}],\"aggregateRating\":{\"@type\":\"AggregateRating\",\"worstRating\":0,\"bestRating\":100,\"ratingValue\":95.34596581379581,\"ratingCount\":112092,\"reviewCount\":3252},\"image\":\"https://image.pmgstatic.com/cache/resized/w420/files/images/film/posters/162/505/162505167_735db9.jpg\"}";
        assertEquals(filmJson, Shawshenk);
    }


    @Test
    public void testFilmParsingWithEmptyString() {
        FilmParser parser = new FilmParser();

        assertThrows(org.json.JSONException.class, () -> {
            parser.parse("");
        });
    }

    @Test
    public void testFilmDownloadByIDWithEmptyString() {
        FilmDownloader downloader = new FilmDownloader();

        String result = downloader.downloadByID("");
        assertNull(result);
    }



}
