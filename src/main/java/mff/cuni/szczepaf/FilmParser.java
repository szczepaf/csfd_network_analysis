package mff.cuni.szczepaf;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class FilmParser implements IParser {

    /**
     * Parses the JSON into a Film object.
     * @param dataDict JSON string containing film data.
     * @return Film object with data from the JSON string.
     */
    public static Film parse(String dataDict) {
        JSONObject jsonObject = new JSONObject(dataDict);

        // Extract fields
        String duration = jsonObject.optString("duration");
        String name = jsonObject.optString("name");
        String dateCreated = jsonObject.optString("dateCreated");
        Rating rating = parseRating(jsonObject.optJSONObject("aggregateRating"));

        // Parse directors, actors
        ArrayList<Director> directors = parseDirectors(jsonObject.optJSONArray("director"));
        ArrayList<Actor> actors = parseActors(jsonObject.optJSONArray("actor"));


        return new Film(duration, name, dateCreated, directors, actors, rating);
    }

    private static ArrayList<Director> parseDirectors(JSONArray directorsArray) {
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

    private static ArrayList<Actor> parseActors(JSONArray actorsArray) {
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

    private static Rating parseRating(JSONObject ratingObject) {
        if (ratingObject != null) {
            float ratingValue = ratingObject.optFloat("ratingValue", 0);
            int ratingCount = ratingObject.optInt("ratingCount", 0);
            return new Rating(ratingValue, ratingCount);
        }
        return null;
    }
}
