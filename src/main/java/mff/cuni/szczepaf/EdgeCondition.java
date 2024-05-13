package mff.cuni.szczepaf;

@FunctionalInterface

public interface EdgeCondition {
    boolean meetsCondition(Film f, Film g);

}
