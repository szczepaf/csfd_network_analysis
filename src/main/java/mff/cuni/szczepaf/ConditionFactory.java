package mff.cuni.szczepaf;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.function.Predicate;

public class ConditionFactory {

    public static FilmCondition createConditionFromJson(String jsonCondition) {
        try{
        JSONObject conditions = new JSONObject(jsonCondition);
        Predicate<Film> compositeCondition = film -> true;

        if (conditions.has("duration")) {
            JSONArray durationConditions = conditions.getJSONArray("duration");
            for (int i = 0; i < durationConditions.length(); i++) {
                String condition = durationConditions.getString(i);
                compositeCondition = compositeCondition.and(parseDurationCondition(condition));
            }
        }

        if (conditions.has("dateCreated")) {
            JSONArray dateConditions = conditions.getJSONArray("dateCreated");
            for (int i = 0; i < dateConditions.length(); i++) {
                String condition = dateConditions.getString(i);
                compositeCondition = compositeCondition.and(parseDateCreatedCondition(condition));
            }
        }

        if (conditions.has("rating")) {
            JSONArray ratingConditions = conditions.getJSONArray("rating");
            for (int i = 0; i < ratingConditions.length(); i++) {
                String condition = ratingConditions.getString(i);
                compositeCondition = compositeCondition.and(parseRatingCondition(condition));
            }
        }

        if (conditions.has("actors")) {
            JSONArray actorConditions = conditions.getJSONArray("actors");
            for (int i = 0; i < actorConditions.length(); i++) {
                JSONObject actorCondition = actorConditions.getJSONObject(i);
                compositeCondition = compositeCondition.and(parseActorCondition(actorCondition));
            }
        }
        if (conditions.has("directors")) {
            JSONArray directorConditions = conditions.getJSONArray("directors");
            for (int i = 0; i < directorConditions.length(); i++) {
                JSONObject directorCondition = directorConditions.getJSONObject(i);
                compositeCondition = compositeCondition.and(parseActorCondition(directorCondition));
            }
        }

        Predicate<Film> finalCompositeCondition = compositeCondition;
        return film -> finalCompositeCondition.test(film);
        }
        catch (JSONException e){
            System.err.println("Invalid Exception! Check your string that is parsed into the exception.");
            throw (e);
        }
    }

    private static Predicate<Film> parseDurationCondition(String condition) {
        String operator = condition.substring(0, 1);
        int value = Integer.parseInt(condition.substring(1));
        switch (operator) {
            case "<":
                return film -> film.getDuration() < value;
            case ">":
                return film -> film.getDuration() > value;
            default:
                throw new IllegalArgumentException("Unsupported operator for duration: " + operator);
        }
    }

    private static Predicate<Film> parseDateCreatedCondition(String condition) {
        String operator = condition.substring(0, 1);
        int year = Integer.parseInt(condition.substring(1));
        return switch (operator) {
            case "=" -> film -> film.getDateCreated() == year;
            case "<" -> film -> film.getDateCreated() < year;
            case ">" -> film -> film.getDateCreated() > year;
            default -> throw new IllegalArgumentException("Unsupported operator for dateCreated: " + operator);
        };
    }

    private static Predicate<Film> parseRatingCondition(String condition) {
        String operator = condition.substring(0, 1);
        double value = Double.parseDouble(condition.substring(1));
        return switch (operator) {
            case "<" -> film -> film.getRating().getRatingValue() < value;
            case ">" -> film -> film.getRating().getRatingValue() > value;
            default -> throw new IllegalArgumentException("Unsupported operator for rating: " + operator);
        };
    }

    private static Predicate<Film> parseActorCondition(JSONObject actorCondition) {
        if (actorCondition.has("contains")) {
            String actorName = actorCondition.getString("contains");
            return film -> film.getActorNames().contains(actorName);
        } else if (actorCondition.has("notcontains")) {
            String actorName = actorCondition.getString("notcontains");
            return film -> !film.getActorNames().contains(actorName);
        }
        throw new IllegalArgumentException("Unsupported actor condition");
    }

    private static Predicate<Film> parseDirectorCondition(JSONObject directorCondition) {
        if (directorCondition.has("contains")) {
            String directorName = directorCondition.getString("contains");
            return film -> film.getDirectorNames().contains(directorName);
        } else if (directorCondition.has("notcontains")) {
            String directorName = directorCondition.getString("notcontains");
            return film -> !film.getDirectorNames().contains(directorName);
        }
        throw new IllegalArgumentException("Unsupported actor condition");
    }
}

