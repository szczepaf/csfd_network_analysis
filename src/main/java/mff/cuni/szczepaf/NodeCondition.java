package mff.cuni.szczepaf;
@FunctionalInterface
public interface NodeCondition {
    boolean meetsCondition(Film film);
}
