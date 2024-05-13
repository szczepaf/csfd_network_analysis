package mff.cuni.szczepaf;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.HashSet;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ConditionFactory {


    /** Creates a NodeCondition used to decide which nodes shall be present in the Film Network.
     * @param jsonCondition the condition represented as a String holding JSON-like conditions.
     * @return The NodeCondition, a functional interface.
     */
    public static NodeCondition createNodeConditionFromJson(String jsonCondition) {
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
                compositeCondition = compositeCondition.and(parseDirectorCondition(directorCondition));
            }
        }

        Predicate<Film> finalCompositeCondition = compositeCondition;
        return film -> finalCompositeCondition.test(film);
        }
        catch (JSONException e){
            System.err.println("Invalid Condition! Check your node condition string.");
            return null;
        }
    }

    private static Predicate<Film> parseDurationCondition(String condition) {
        String operator = condition.substring(0, 1);
        try {

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
        catch (NumberFormatException e){
                System.err.println("Wrong Duration! Check your duration string.");
                throw (e);
            }
    }

    private static Predicate<Film> parseDateCreatedCondition(String condition) {
        String operator = condition.substring(0, 1);
        try {
            int year = Integer.parseInt(condition.substring(1));

            return switch (operator) {
                case "=" -> film -> film.getDateCreated() == year;
                case "<" -> film -> film.getDateCreated() < year;
                case ">" -> film -> film.getDateCreated() > year;
                default -> throw new IllegalArgumentException("Unsupported operator for dateCreated: " + operator);
            };
        }
        catch (NumberFormatException e){
            System.err.println("Wrong Year! Check your year string.");
            throw (e);
        }
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
        throw new IllegalArgumentException("Unsupported director condition");
    }

    /**
     * Equivalent of the factory function for Nodes, but for Edges.
     */
    public static EdgeCondition createEdgeConditionFromJson(String jsonCondition) {
        try {
            JSONObject conditions = new JSONObject(jsonCondition);
            BiPredicate<Film, Film> compositeCondition = (f, g) -> true;

            if (conditions.has("commonActors")) {
                int n = conditions.getInt("commonActors");
                compositeCondition = compositeCondition.and((f, g) -> {
                    HashSet<String> film1Actors = new HashSet<>(f.getActorNames());
                    HashSet<String> film2Actors = new HashSet<>(g.getActorNames());
                    film1Actors.retainAll(film2Actors);  // Get the intersection of both sets
                    return film1Actors.size() >= n;
                });
            }

            if (conditions.has("commonDirector")) {
                boolean commonDirectorRequired = conditions.getBoolean("commonDirector");
                if (commonDirectorRequired) {
                    compositeCondition = compositeCondition.and((f, g) -> {
                        HashSet<String> film1Directors = new HashSet<>(f.getDirectorNames());
                        HashSet<String> film2Directors = new HashSet<>(g.getDirectorNames());
                        film1Directors.retainAll(film2Directors);
                        return !film1Directors.isEmpty();
                    });
                }
            }

            BiPredicate<Film, Film> finalCompositeCondition = compositeCondition;
            return (f, g) -> finalCompositeCondition.test(f, g);
        } catch (JSONException e) {
            System.err.println("Invalid Condition! Check your edge condition string.");
            return null;
        }
    }

}

