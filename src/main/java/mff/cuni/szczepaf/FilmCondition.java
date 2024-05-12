package mff.cuni.szczepaf;
@FunctionalInterface
public interface FilmCondition {
    boolean meetsCondition(Film film);
}
