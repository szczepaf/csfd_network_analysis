package mff.cuni.szczepaf;

import java.util.ArrayList;
import java.util.List;

public class Film implements IMediaEntity {
    private int duration;
    private String name;
    private int dateCreated;
    private List<Director> directors;
    private List<Actor> actors;
    private Rating rating;

    // Constructor
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
