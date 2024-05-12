package mff.cuni.szczepaf;

import java.util.List;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FilmParser implements IParser {

    /**
     * Parses the JSON into a Film object.
     * @param dataDict JSON string containing film data.
     * @return Film object with data from the JSON string.
     */
    @Override
    public Film parse(String dataDict) {

        try {
            JSONObject jsonObject = new JSONObject(dataDict);

            // Extract fields
            String durationStr = jsonObject.optString("duration");
            int duration = 0;
            if (durationStr != null && durationStr.startsWith("PT") && durationStr.endsWith("M")) {
                duration = Integer.parseInt(durationStr.substring(2, durationStr.length() - 1)); }

            String name = jsonObject.optString("name");
            int dateCreated = Integer.parseInt(jsonObject.optString("dateCreated"));
            Rating rating = parseRating(jsonObject.optJSONObject("aggregateRating"));

            // Parse directors, actors
            ArrayList<Director> directors = parseDirectors(jsonObject.optJSONArray("director"));
            ArrayList<Actor> actors = parseActors(jsonObject.optJSONArray("actor"));

            return new Film(duration, name, dateCreated, directors, actors, rating);
        }
        catch (JSONException e) {
            System.err.println("JSON parsing error for : " + dataDict + ": " + e.getMessage());
            return null;
        }
        catch (NumberFormatException e) {
            System.err.println("Error parsing duration: " + e.getMessage());
            return null;
        }
    }


    private ArrayList<Director> parseDirectors(JSONArray directorsArray) {
        ArrayList<Director> directors = new ArrayList<>();
        if (directorsArray != null) {
            for (int i = 0; i < directorsArray.length(); i++) {
                JSONObject directorObject = directorsArray.getJSONObject(i);
                String name = directorObject.optString("name");
                directors.add(new Director(name));
            }
        }
        return directors;
    }

    private ArrayList<Actor> parseActors(JSONArray actorsArray) {
        ArrayList<Actor> actors = new ArrayList<>();
        if (actorsArray != null) {
            for (int i = 0; i < actorsArray.length(); i++) {
                JSONObject actorObject = actorsArray.getJSONObject(i);
                String name = actorObject.optString("name");
                actors.add(new Actor(name));
            }
        }
        return actors;
    }

    private Rating parseRating(JSONObject ratingObject) {
        if (ratingObject != null) {
            float ratingValue = ratingObject.optFloat("ratingValue", 0);
            int ratingCount = ratingObject.optInt("ratingCount", 0);
            return new Rating(ratingValue, ratingCount);
        }
        return null;
    }

    public void dumpFilm(Film film, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(film.toString());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to file " + filename + ": " + e);
        }
    }

    public void dumpFilm(Film film, String filename, String filmID) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(filmID + ": " + film.toString());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to file " + filename + ": " + e);
        }
    }


    public void dumpFilms(List<Film> films, String filename) {
        for (Film film : films) {
            dumpFilm(film, filename);
        }
    }

    /**
     * Extracts the movie ID from a given film URL.
     * @param url the URL from which the movie ID should be extracted.
     * @return the movie ID as a string.
     */
    public String extractFilmIDFromURL(String url) {
        int idStart = url.indexOf("film/") + 5;
        int idEnd = url.indexOf("-", idStart);

        if (idStart > 4 && idEnd > idStart) { // Check bounds
            return url.substring(idStart, idEnd);
        } else {
            System.err.println("Invalid URL format for extracting film ID: " + url);
            return null;
        }
    }



    public Film parseFilmFromDump(String dataDict) {
        try {
            JSONObject jsonObject = new JSONObject(dataDict);

            int duration = jsonObject.optInt("duration", 0);
            String name = jsonObject.optString("name");
            int dateCreated = jsonObject.optInt("dateCreated", 0);

            Rating rating = parseSimpleRating(jsonObject.optFloat("rating"));

            // Parse directors, actors
            ArrayList<Director> directors = parseDirectorsFromDump(jsonObject.optJSONArray("directors"));
            ArrayList<Actor> actors = parseActorsFromDump(jsonObject.optJSONArray("actors"));

            return new Film(duration, name, dateCreated, directors, actors, rating);
        } catch (JSONException e) {
            System.err.println("JSON parsing error for : " + dataDict + ": " + e.getMessage());
            return null;
        }
    }

    private Rating parseSimpleRating(float ratingValue) {
        int ratingCount = 0;
        return new Rating(ratingValue, ratingCount);
    }

    private ArrayList<Director> parseDirectorsFromDump(JSONArray directorsArray) {
        ArrayList<Director> directors = new ArrayList<>();
        if (directorsArray != null) {
            for (int i = 0; i < directorsArray.length(); i++) {
                String name = directorsArray.optString(i);
                directors.add(new Director(name));
            }
        }
        return directors;
    }

    private ArrayList<Actor> parseActorsFromDump(JSONArray actorsArray) {
        ArrayList<Actor> actors = new ArrayList<>();
        if (actorsArray != null) {
            for (int i = 0; i < actorsArray.length(); i++) {
                String name = actorsArray.optString(i);
                actors.add(new Actor(name));
            }
        }
        return actors;
    }

}


