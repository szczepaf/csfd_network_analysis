package mff.cuni.szczepaf;

/**
 * Represents Actors of a film.
 * They will be crucial in determining which Films~Nodes are linked
 * and which nodes should appear in the Network at all.
 */

public class Actor extends Person {

    public Actor(String name) {
        super(name);
    }
}