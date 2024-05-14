package mff.cuni.szczepaf;

/**
 * Represents Director(s) of a film.
 * They will be crucial in determining which Films~Nodes are linked
 * and which nodes should appear in the Network at all.
 */

public class Director extends Person {
    public Director(String name) {
        super(name);
    }
}