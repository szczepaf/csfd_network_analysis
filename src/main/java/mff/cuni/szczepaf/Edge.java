package mff.cuni.szczepaf;

/**
 * An edge in the FilmNetwork is two Films.
 * They will most often be connected only if they meet a certain condition
 * e.g. share at least 2 actors
 */
public class Edge {
    private Film film1;
    private Film film2;

    public Edge(Film film1, Film film2) {
        this.film1 = film1;
        this.film2 = film2;
    }

    public Film getFilm1() {
        return film1;
    }

    public Film getFilm2() {
        return film2;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "film1=" + film1.getName() +
                ", film2=" + film2.getName() +
                '}';
    }
}