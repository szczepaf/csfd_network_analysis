package mff.cuni.szczepaf;

import java.util.List;

public class Film implements IMediaEntity {
    private String duration;
    private String name;
    private String dateCreated;
    private List<Director> directors;
    private List<Actor> actors;
    private Rating rating;

    // Constructor
    public Film(String duration, String name, String dateCreated,
                List<Director> directors, List<Actor> actors, Rating rating) {
        this.duration = duration;
        this.name = name;
        this.dateCreated = dateCreated;
        this.directors = directors;
        this.actors = actors;
        this.rating = rating;
    }

    public String getDuration() {
        return duration;
    }

    public String getName() {
        return name;
    }


    public String getDateCreated() {
        return dateCreated;
    }

    public List<Director> getDirectors() {
        return directors;
    }

    public List<Actor> getActors() {
        return actors;
    }


    public Rating getRating() {
        return rating;
    }


    @Override
    public String toString() {
        StringBuilder filmString = new StringBuilder();
        filmString.append("{");

        filmString.append("\"name\": \"").append(name).append("\", ");
        filmString.append("\"duration\": \"").append(duration).append("\", ");
        filmString.append("\"dateCreated\": \"").append(dateCreated).append("\", ");

        filmString.append("\"directors\": [");
        for (int i = 0; i < directors.size(); i++) {
            if (i > 0) filmString.append(", ");
            filmString.append("\"").append(directors.get(i).getName()).append("\"");
        }
        filmString.append("], ");

        filmString.append("\"actors\": [");
        for (int i = 0; i < actors.size(); i++) {
            if (i > 0) filmString.append(", ");
            filmString.append("\"").append(actors.get(i).getName()).append("\"");
        }
        filmString.append("], ");

        filmString.append("\"rating\": ").append(rating.getRatingValue());

        filmString.append("}");

        return filmString.toString();
    }


}
