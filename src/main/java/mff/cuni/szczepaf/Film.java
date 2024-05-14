package mff.cuni.szczepaf;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A class representing one Film from the CSFD portal.
 * It holds information about the film such as its length, rating, etc., but also will serve as a node in the Film Graph.
 */
public class Film implements IMediaEntity {
    private int duration;
    // Duration of the film in minutes
    private String name;
    // Name of the film
    private int dateCreated;
    // Year of creation
    private List<Director> directors;
    // 1 or more, usually just 1
    private List<Actor> actors;
    // N actors
    private Rating rating;
    // Rating class - rating value and rating count. Rating count is currently not important anyhow.

    public Film(int duration, String name, int dateCreated,
                List<Director> directors, List<Actor> actors, Rating rating) {
        this.duration = duration;
        this.name = name;
        this.dateCreated = dateCreated;
        this.directors = directors;
        this.actors = actors;
        this.rating = rating;
    }

    public int getDuration() {
        return duration;
    }

    public String getName() {
        return name;
    }


    public int getDateCreated() {
        return dateCreated;
    }

    public List<Director> getDirectors() {
        return directors;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public List<String> getActorNames(){
        ArrayList<String> actorNames = new ArrayList<String>();
        for (Actor actor : actors){
            actorNames.add(actor.getName());
        }
        return actorNames;
    }

    public List<String> getDirectorNames(){
        ArrayList<String> directorNames = new ArrayList<String>();
        for (Director director : directors){
            directorNames.add(director.getName());
        }
        return directorNames;
    }

    public Rating getRating() {
        return rating;
    }

    /**
     * Lets say two Films are equal if they have the same Name, the same Duration, Director and Date Created.
     * It is very unlikely to impossible two Film would have the same values of these films but would not be the same Film.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return duration == film.duration &&
                dateCreated == film.dateCreated &&
                name.equals(film.name) &&
                directors.equals(film.directors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(duration, name, dateCreated, directors);
    }

    /**
     * Use a format that will be:
     * 1) Legible
     * 2) Easily loadable in the future
     *
     * @return a string representation of the Film.
     */
    @Override
    public String toString() {
        StringBuilder filmString = new StringBuilder();
        filmString.append("{");

        filmString.append("\"name\": \"").append(name.replace("\"", "'")).append("\", ");
        filmString.append("\"duration\":").append(duration).append(", ");
        filmString.append("\"dateCreated\": ").append(dateCreated).append(", ");

        filmString.append("\"directors\": [");
        for (int i = 0; i < directors.size(); i++) {
            if (i > 0) filmString.append(", ");
            filmString.append("\"").append(directors.get(i).getName().replace("\"", "'")).append("\"");
        }
        filmString.append("], ");

        filmString.append("\"actors\": [");

        for (int i = 0; i < actors.size(); i++) {
            if (i > 0) filmString.append(", ");
            filmString.append("\"").append(actors.get(i).getName().replace("\"", "'")).append("\"");
        }
        filmString.append("], ");

        filmString.append("\"rating\": ").append(rating.getRatingValue());

        filmString.append("}");

        return filmString.toString();
    }


}
